package com.huang.stock.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huang.stock.constant.StockConstant;
import com.huang.stock.pojo.domain.UserRoleDomain;
import com.huang.stock.pojo.entity.SysUserInfos;
import com.huang.stock.service.SysUserService;
import com.huang.stock.mapper.SysUserMapper;
import com.huang.stock.pojo.entity.SysUser;
import com.huang.stock.utils.IdWorker;
import com.huang.stock.vo.req.SysAddUserReqVo;
import com.huang.stock.vo.req.SysLoginReqVo;
import com.huang.stock.vo.req.SysUpdateUserReqVo;
import com.huang.stock.vo.resp.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.math.BigInteger;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Override
    public SysUser selectUserByname(String userName) {
        SysUser user =sysUserMapper.findByUsernameSysUser(userName);
        return user;
    }

    @Override
    public R<SysLoginRespVo> login(SysLoginReqVo vo) {
        //判断参数是否合法
        if (vo==null
                || StringUtils.isBlank(vo.getUsername())
                ||StringUtils.isBlank(vo.getPassword())
                ||StringUtils.isBlank(vo.getCode())){
            return R.error(ResponseCode.DATA_ERROR);
        }
        /*//判断redis保存的验证码与输入的验证码是否相同(比较的时候忽略大小写)
        String redisCode = (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX+vo.getSessionId());
        if (StringUtils.isBlank(redisCode)){
            //验证码过期
            return R.error(ResponseCode.CHECK_CODE_TIMEOUT);
        }
        if (!redisCode.equalsIgnoreCase(vo.getCode())){
            //验证码错误
            return R.error(ResponseCode.CHECK_CODE_ERROR);
        }*/
        //根据用户名去数据库中查询用户信息 获取密码加密后的密文
        SysUser user = sysUserMapper.findByUsername(vo.getUsername());
        //判断用户是否存在
        if (user == null){
            //用户不存在
            return R.error(ResponseCode.ACCOUNT_NOT_EXISTS);
        }
        //调用密码匹配器匹配输入明文密码和数据密文密码
        if (!passwordEncoder.matches(vo.getPassword(), user.getPassword())){
            return R.error(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //响应
        SysLoginRespVo respVo = new SysLoginRespVo();
        respVo.setUsername(user.getUsername());
        respVo.setPhone(user.getPhone());
        respVo.setNickName(user.getNickName());
        respVo.setId(user.getId());
        //List<UserMenusDomain>
        /*respVo.setMenus(user.get);*/
        BeanUtils.copyProperties(user,respVo);
        return R.ok(respVo);
    }

    @Override
    public R<Map> getCaptCode() {
        //1.生成图片验证码 参数分别是:图片的宽 图片的高 图片验证码的长度 干扰线数量
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(250, 40, 4, 5);
        //设置背景颜色
        captcha.setBackground(Color.LIGHT_GRAY);
        //获取图片中的验证码 默认生成验证码包含文字数字 长度为4
        String checkCode = captcha.getCode();
        //获取经过base64编码处理的图片数据
        String imageData = captcha.getImageBase64();
        //2.生成sessionID转化为String 避免数据精度丢失
        String sessionId = String.valueOf(idWorker.nextId());
        log.info("当前生成的图片验证码:{},会话id:{}",checkCode,sessionId);
        //3.将sessionId作为key 校验码作为value保存在redis中 (使用redis模拟session的行为 还可以设置过期时间)
        redisTemplate.opsForValue().set(StockConstant.CHECK_PREFIX +sessionId,checkCode,5, TimeUnit.MINUTES);
        //4.组装响应数据
        HashMap<String,String> data = new HashMap<>();
        data.put("imageData",imageData);
        data.put("sessionId",sessionId);
        //设置响应数据格式;

        return R.ok(data);
    }
    /*
     * 多条件综合查询用户分页信息，条件包含：分页信息 用户创建日期范围
     * */
    @Override
    public R<PageResult<SysUserInfos>> getUserInfos(Integer page, Integer pageSize, Date startTime, Date endTime) {
        startTime = DateTime.parse("2020-04-04 17:18:34", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        endTime = DateTime.parse("2023-03-24 17:31:32", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        PageHelper.startPage(page, pageSize);//设置分页参数
        List<SysUserInfos> list = sysUserMapper.getUserInfos(startTime,endTime);
        PageInfo<SysUserInfos> pageInfo = new PageInfo<>(list);
        PageResult<SysUserInfos> pageResult = new PageResult<>(pageInfo);
        return R.ok(pageResult);
    }

    /*
     * 添加用户信息
     * */
    @Override
    public R addUserInfos(SysAddUserReqVo vo) {
        //判断参数是否合法
        if (vo==null
                || StringUtils.isBlank(vo.getUsername())
                ||StringUtils.isBlank(vo.getPassword())
                ){
            return R.error(ResponseCode.DATA_ERROR);
        }
        // 检查用户名是否已存在
        SysUser existingUser = sysUserMapper.findByUsername(vo.getUsername());
        if (existingUser != null) {
            return R.error(ResponseCode.ACCOUNT_EXISTS_ERROR);
        }
        SysUser newUser = new SysUser();
        //响应
        newUser.setUsername(vo.getUsername());
        newUser.setPassword(vo.getPassword());
        newUser.setPhone(vo.getPhone());
        newUser.setEmail(vo.getEmail());
        newUser.setNickName(vo.getNickName());
        newUser.setRealName(vo.getRealName());
        newUser.setSex(vo.getSex());
        newUser.setCreateWhere(vo.getCreateWhere());
        newUser.setStatus(vo.getStatus());
        BeanUtils.copyProperties(vo, newUser);
        sysUserMapper.insert(newUser);
        return R.ok(ResponseCode.SUCCESS);
    }

    /*
     * 获取用户具有的角色信息
     * */
    @Override
    public R<Map<String, List>> getUserRole(Long userId) {
        //查询数据
        List<Long> ownRoleIds =sysUserMapper.selectRoleIdsByUserId(userId);
        List<UserRoleDomain> allRole = sysUserMapper.getAllUserRole();
        //map封装
        Map<String,List> data = new LinkedHashMap<>();
        data.put("ownRoleIds",ownRoleIds);
        data.put("allRole",allRole);
        return R.ok(data);
    }

    /*
     * 更新用户角色信息
     * */
    @Override
    public R updateUserRoles(SysUpdateUserReqVo vo,Long userId) {
        //判断参数是否合法
        if (vo==null
                || Objects.isNull(vo.getUserId())
                || CollectionUtils.isEmpty(vo.getRoleIds())
        ){
            return R.error(ResponseCode.DATA_ERROR);
        }
        List<Long> roleIds =sysUserMapper.selectRoleIdsByUserId(userId);
        SysUser newUser = new SysUser();
        vo.setUserId(newUser.getId());
        vo.setRoleIds(roleIds);
        return R.ok(ResponseCode.SUCCESS);
    }


}
