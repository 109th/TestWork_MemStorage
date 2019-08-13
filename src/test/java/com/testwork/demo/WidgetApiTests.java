package com.testwork.demo;

import com.testwork.demo.http.BaseResponse;
import com.testwork.demo.http.requests.ChangeWidgetRequest;
import com.testwork.demo.http.requests.CreateWidgetRequest;
import com.testwork.demo.http.responses.CreateWidgetResponse;
import com.testwork.demo.http.responses.WidgetListResponse;
import com.testwork.demo.http.responses.WidgetResponse;
import com.testwork.demo.services.MemoryStorageInterface;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WidgetApiTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MemoryStorageInterface memoryStorage;

    private static ArrayList<String> UuidList = new ArrayList<String>();


    @Test
    public void t0_IndexTest() {
        BaseResponse response = this.restTemplate.getForObject("/", BaseResponse.class);
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getMessage()).isEqualTo("Hello there, general Kenobi");
    }

    @Test
    public void t1_CreateWidgetTest() {
        CreateWidgetResponse response;

        CreateWidgetRequest request = new CreateWidgetRequest();
        request.setHeight(10);
        request.setWidth(20);
        request.setX(30);
        request.setY(40);
        request.setZIndex(42);
        response = this.restTemplate.postForObject("/widget/create", request, CreateWidgetResponse.class);
        assertThat(response.getUuid()).isNotEmpty();
        UuidList.add(response.getUuid());

        request = new CreateWidgetRequest();
        request.setHeight(50);
        request.setWidth(60);
        request.setX(70);
        request.setY(80);
        response = this.restTemplate.postForObject("/widget/create", request, CreateWidgetResponse.class);
        assertThat(response.getUuid()).isNotEmpty();
        UuidList.add(response.getUuid());

        request = new CreateWidgetRequest();
        request.setHeight(90);
        request.setWidth(100);
        request.setX(110);
        request.setY(120);
        response = this.restTemplate.postForObject("/widget/create", request, CreateWidgetResponse.class);
        assertThat(response.getUuid()).isNotEmpty();
        UuidList.add(response.getUuid());
    }

    @Test
    public void t2_WidgetListTest() {
        WidgetListResponse response = this.restTemplate.getForObject("/widget/list", WidgetListResponse.class);
        assertThat(response.getSize()).isEqualTo(3);
        assertThat(response.getList()).isExactlyInstanceOf(ArrayList.class);
        assertThat(response.getList().size()).isEqualTo(3);
    }

    @Test
    public void t3_WidgetRequestTest() {
        HashMap<String, String> vars = new HashMap<>();
        WidgetResponse response;

        vars.put("id", UuidList.get(0));
        response = this.restTemplate.getForObject("/widget/get/{id}", WidgetResponse.class, vars);
        assertThat(response.getUuid()).isEqualTo(UuidList.get(0));

        vars.put("id", UuidList.get(1));
        response = this.restTemplate.getForObject("/widget/get/{id}", WidgetResponse.class, vars);
        assertThat(response.getUuid()).isEqualTo(UuidList.get(1));

        vars.put("id", UuidList.get(2));
        response = this.restTemplate.getForObject("/widget/get/{id}", WidgetResponse.class, vars);
        assertThat(response.getUuid()).isEqualTo(UuidList.get(2));
        assertThat(response.getHeight()).isEqualTo(90);
        assertThat(response.getWidth()).isEqualTo(100);
        assertThat(response.getX()).isEqualTo(110);
        assertThat(response.getY()).isEqualTo(120);
        assertThat(response.getZIndex()).isEqualTo(2);
    }

    @Test
    public void t4_WidgetRemoveTest() {
        HashMap<String, String> vars = new HashMap<>();
        vars.put("id", UuidList.get(0));

        assertThat(memoryStorage.count()).isEqualTo(3);
        this.restTemplate.delete("/widget/remove/{id}", vars);
        assertThat(memoryStorage.count()).isEqualTo(2);
    }

    @Test
    public void t5_WidgetChangeTest() {
        HashMap<String, String> vars = new HashMap<>();
        vars.put("id", UuidList.get(2));

        ChangeWidgetRequest widgetRequest = new ChangeWidgetRequest();
        widgetRequest.setX(111);
        widgetRequest.setY(121);

        BaseResponse response = this.restTemplate.postForObject("/widget/change/{id}", widgetRequest, BaseResponse.class, vars);
        assertThat(response.getStatus()).isEqualTo(BaseResponse.SUCCESS_STATUS);

        WidgetResponse widgetResponse = this.restTemplate.getForObject("/widget/get/{id}", WidgetResponse.class, vars);
        assertThat(widgetResponse.getUuid()).isEqualTo(UuidList.get(2));
        assertThat(widgetResponse.getHeight()).isEqualTo(90);
        assertThat(widgetResponse.getWidth()).isEqualTo(100);
        assertThat(widgetResponse.getX()).isEqualTo(111);
        assertThat(widgetResponse.getY()).isEqualTo(121);
        assertThat(widgetResponse.getZIndex()).isEqualTo(1);


        widgetRequest = new ChangeWidgetRequest();
        widgetRequest.setZIndex(0);
        response = this.restTemplate.postForObject("/widget/change/{id}", widgetRequest, BaseResponse.class, vars);
        assertThat(response.getStatus()).isEqualTo(BaseResponse.SUCCESS_STATUS);

        widgetResponse = this.restTemplate.getForObject("/widget/get/{id}", WidgetResponse.class, vars);
        assertThat(widgetResponse.getUuid()).isEqualTo(UuidList.get(2));
        assertThat(widgetResponse.getHeight()).isEqualTo(90);
        assertThat(widgetResponse.getWidth()).isEqualTo(100);
        assertThat(widgetResponse.getX()).isEqualTo(111);
        assertThat(widgetResponse.getY()).isEqualTo(121);
        assertThat(widgetResponse.getZIndex()).isEqualTo(0);
    }
}
