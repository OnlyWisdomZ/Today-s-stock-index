package com.huang.stock.test;

import com.huang.stock.pojo.Account;
import com.huang.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class TestRestTemplate {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StockTimerTaskService stockTimerTaskService;
    /*
    * get请求携带参数
    * */
    @Test
    public void test01(){
        String url="http://localhost:6767/account/getByUserNameAndAddress?userName=zhangsan&address=sh";
        ResponseEntity<String> result =restTemplate.getForEntity(url, String.class);//参数1 地址 参数2 返回值类型
        HttpHeaders headers =result.getHeaders();
        System.out.println(headers);
        int statusCode =result.getStatusCodeValue();
        System.out.println(statusCode);
        //获取响应数据
        String respData =result.getBody();
        System.out.println(respData);
    }
    /*
     * 测试响应数据自动封装到vo对象中
     * */
    @Test
    public void test02(){
        String url="http://localhost:6767/account/getByUserNameAndAddress?userName=zhangsan&address=sh";
        Account account =restTemplate.getForObject(url, Account.class);//参数1 地址 参数2 返回值类型
        System.out.println(account);
    }
    /*
     * 请求头设置参数 访问指定接口
     * */
    @Test
    public void test03(){
        String url ="http://localhost:6767/account/getHeader";
        HttpHeaders headers =new HttpHeaders();
        headers.add("username","zhangsan");
        HttpEntity<Map> entity =new HttpEntity<>(headers);
        System.out.println(entity);
        ResponseEntity<String> responseEntity =restTemplate.exchange(url, HttpMethod.GET,entity, String.class);
        String result =responseEntity.getBody();
        System.out.println(result);
    }
    /*
     * 模拟post连接 pom表单提交
     * */
   @Test
    public void test04(){
       String url ="http://localhost:6767/account/addAccount";
       HttpHeaders headers =new HttpHeaders();
       headers.add("Content-type","application/x-www-form-urlencoded");//让接收方以pom表单形式解析
       LinkedMultiValueMap<String,Object> map =new LinkedMultiValueMap<>();
       map.add("id","10");
       map.add("userName","Huang");
       map.add("address","harbin");
       HttpEntity<LinkedMultiValueMap> entity =new HttpEntity<>(map,headers);
       ResponseEntity<Account> responseEntity =restTemplate.exchange(url, HttpMethod.POST,entity, Account.class);
       //System.out.println(responseEntity);
       Account body =responseEntity.getBody();
       System.out.println(body);
   }
    /*
     * post请求 发送json
     * */
   @Test
    public void test05(){
       String url ="http://localhost:6767/account/updateAccount";
       HttpHeaders headers =new HttpHeaders();
       headers.add("Content-type","application/json;charset=utf-8");
       String jsonRequest="{\"address\":\"上海\",\"id\":\"1\",\"userName\":\"zhangsan\"}";
       HttpEntity<String> entity =new HttpEntity<>(jsonRequest,headers);
       ResponseEntity<Account> responseEntity =restTemplate.exchange(url,HttpMethod.POST,entity,Account.class);
       Account body =responseEntity.getBody();
       System.out.println(body);
   }
   /*
   * 测试cookie
   * */
   @Test
    public void test06(){
       String url = "http://localhost:6767/account/getCookie";
       ResponseEntity<String> responseEntity=restTemplate.getForEntity(url, String.class);
       List<String> cookies =responseEntity.getHeaders().get("Set-Cookie");
       String res =responseEntity.getBody();
       System.out.println(res);
       System.out.println(cookies);
   }
    /*
     * 测试采集国内大盘数据
     * */
    @Test
    public void testInnerGetMarketInfo() throws InterruptedException {
        //stockTimerTaskService.getInnerMarketInfo();
        stockTimerTaskService.getStockRtIndex();
        //目的:让主线程休眠 等待子线程完成任务
        Thread.sleep(5000);
    }
    @Test
    public void testInnerBlockInfos() throws InterruptedException {
        //stockTimerTaskService.getInnerMarketInfo();
        stockTimerTaskService.getBlockInfos();
        //目的:让主线程休眠 等待子线程完成任务
        Thread.sleep(5000);
    }
    @Test
    public void testOuterMarketInfos(){
        stockTimerTaskService.getOuterMarketInfos();//外盘时间解析有大问题
    }
}