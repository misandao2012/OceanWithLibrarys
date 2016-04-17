package jian.zhang.oceanwithlibrarys;

import android.app.Application;
import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import jian.zhang.oceanwithlibrarys.database.Station;
import jian.zhang.oceanwithlibrarys.global.OceanAPI;
import jian.zhang.oceanwithlibrarys.service.LoadDataService;
import rx.Observable;

import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)


public class LoadDataServiceRoboTest {

    @Mock
    OceanAPI oceanAPI;
    private LoadDataService service;

    @Before
    public void setUp() {
        Application application = RuntimeEnvironment.application;
        Intent intent = new Intent(application, LoadDataService.class);
        service = new LoadDataService();
        service.onStartCommand(intent, 0, 0);
    }

    @After
    public void tearDown() {
        service.stopSelf();
    }

    @Test
    public void general() {
        List<Station> stations = new ArrayList<>();
        doReturn(Observable.just(stations)).when(oceanAPI).getStations();
        service.loadData();
    }
}
