package com.testwork.demo;

import com.testwork.demo.models.Widget;
import com.testwork.demo.services.MemoryStorageInterface;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MemoryStorageTests {
    @Autowired
    private MemoryStorageInterface memoryStorage;

    private static ArrayList<UUID> UuidList = new ArrayList<UUID>();

    @Test
    public void t1_contextLoads() {
        assertThat(memoryStorage).isInstanceOf(MemoryStorageInterface.class);
    }

    @Test
    /*
    Должны получить массив из widget4, widget1, widget2, widget3
     */
    public void t2_AddWidgetsTest() {
        Widget widget1 = new Widget();
        widget1.setX(1);
        widget1.setY(1);
        UuidList.add(memoryStorage.add(widget1));
        assertThat(widget1.getZIndex()).isEqualTo(0);

        Widget widget2 = new Widget();
        widget2.setX(2);
        widget2.setY(2);
        UuidList.add(memoryStorage.add(widget2, 1));
        assertThat(widget2.getZIndex()).isEqualTo(1);

        Widget widget3 = new Widget();
        widget3.setX(3);
        widget3.setY(3);
        UuidList.add(memoryStorage.add(widget3, 100500));
        assertThat(widget3.getZIndex()).isEqualTo(2);

        Widget widget4 = new Widget();
        widget4.setX(4);
        widget4.setY(4);
        UuidList.add(memoryStorage.add(widget4, -100500));
        assertThat(widget4.getZIndex()).isEqualTo(0);
    }

    @Test
    public void t3_CountTest() {
        assertThat(memoryStorage.count()).isEqualTo(4);
        assertThat(UuidList.size()).isEqualTo(4);
    }

    @Test
    public void t4_PositionTest() {
        Widget widget;

        widget = memoryStorage.getByUuid(UuidList.get(0));
        assertThat(memoryStorage.getWidgetPosition(widget)).isEqualTo(1);

        widget = memoryStorage.getByUuid(UuidList.get(1));
        assertThat(memoryStorage.getWidgetPosition(widget)).isEqualTo(2);

        widget = memoryStorage.getByUuid(UuidList.get(2));
        assertThat(memoryStorage.getWidgetPosition(widget)).isEqualTo(3);

        widget = memoryStorage.getByUuid(UuidList.get(3));
        assertThat(memoryStorage.getWidgetPosition(widget)).isEqualTo(0);

        assertThat(memoryStorage.getByUuid(UUID.fromString("00000000-0000-0000-0000-000000000000"))).isNull();
        assertThat(memoryStorage.getWidgetPosition(new Widget())).isEqualTo(-1);
    }

    @Test
    public void t5_RemoveTest() {
        memoryStorage.remove(UuidList.get(0));
        Widget widget = memoryStorage.getByUuid(UuidList.get(1));
        memoryStorage.remove(widget);

        assertThat(memoryStorage.count()).isEqualTo(2);
    }

    @Test
    public void t6_ListTest() {
        ArrayList<Widget> widgets = memoryStorage.getList();
        assertThat(widgets.size()).isEqualTo(2);
        assertThat(widgets.get(1).getUuid()).isEqualTo(UuidList.get(2));
    }
}
