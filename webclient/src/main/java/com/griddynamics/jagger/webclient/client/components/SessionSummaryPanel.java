package com.griddynamics.jagger.webclient.client.components;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.griddynamics.jagger.webclient.client.WorkloadTaskDataService;
import com.griddynamics.jagger.webclient.client.dto.SessionDataDto;
import com.griddynamics.jagger.webclient.client.dto.TaskDataDto;
import com.griddynamics.jagger.webclient.client.dto.WorkloadTaskDataDto;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: kirilkadurilka
 * Date: 14.03.13
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class SessionSummaryPanel extends VerticalPanel implements SessionPanel{

    private HashMap<String, TestPanel> testPanels = new HashMap<String, TestPanel>();
    private final Panel testPanel = new VerticalPanel();
    private SessionDataDto session;

    public SessionSummaryPanel(SessionDataDto sessionData){
        session = sessionData;
        initStyle();
        initData(sessionData);
    }

    private void initStyle(){
        setWidth("1350px");
    }

    private void initData(SessionDataDto sessionData){

        Label name = new Label("Session #"+sessionData.getSessionId());
        name.setStyleName("sessionNameHeader");
        add(name);

        Grid summaryGrid = new Grid(6, 2);
        summaryGrid.getElement().getStyle().setProperty("margin", "40px");

        Label sessionLabel = new Label("Session id");
        sessionLabel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        summaryGrid.setWidget(0, 0, sessionLabel);
        summaryGrid.setWidget(0, 1, new Label(sessionData.getSessionId()));

        Label startTimeLabel = new Label("Session start time");
        startTimeLabel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        summaryGrid.setWidget(1, 0, startTimeLabel);
        summaryGrid.setWidget(1, 1, new Label(sessionData.getStartDate()));

        Label endTimeLabel = new Label("Session end time");
        endTimeLabel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        summaryGrid.setWidget(2, 0, endTimeLabel);
        summaryGrid.setWidget(2, 1, new Label(sessionData.getEndDate()));

        Label numberTasksLabel = new Label("Number of tasks");
        numberTasksLabel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        summaryGrid.setWidget(3, 0, numberTasksLabel);
        summaryGrid.setWidget(3, 1, new Label(Integer.toString(sessionData.getTasksExecuted())));

        Label failuresLabel = new Label("Number of task failures");
        failuresLabel.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        summaryGrid.setWidget(4, 0, failuresLabel);
        summaryGrid.setWidget(4, 1, new Label(Integer.toString(sessionData.getTasksFailed())));

        Label activeKernels = new Label("Number of active kernels");
        activeKernels.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        summaryGrid.setWidget(5, 0, activeKernels);
        summaryGrid.setWidget(5, 1, new Label(Integer.toString(sessionData.getActiveKernelsCount())));

        add(summaryGrid);

        Label testPanelName = new Label("Tests");
        testPanelName.addStyleName("testNameHeader");
        add(testPanelName);

        add(testPanel);
    }

    private VerticalPanel createTestPanel(String sessionId){
        final VerticalPanel testPanel = new VerticalPanel();
        WorkloadTaskDataService.Async.getInstance().getWorkloadTaskData(sessionId, new AsyncCallback<List<WorkloadTaskDataDto>>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<WorkloadTaskDataDto> result) {
                for (WorkloadTaskDataDto data : result){
                    TestPanel testPanel = new TestPanel(data);
                    testPanels.put(data.getTaskId(), testPanel);
                    testPanel.add(testPanel);
                }
            }
        });

        return testPanel;
    }

    @Override
    public void update(Set<TaskDataDto> tests) {
        removeOld(tests);
        addNew(tests);
    }

    private void removeOld(Set<TaskDataDto> tests){
        //remove all
        for (String testName : testPanels.keySet()){
            testPanels.get(testName).setVisible(false);
        }
    }

    private void addNew(Set<TaskDataDto> tests){
        for (TaskDataDto test : tests){
            if (testPanels.containsKey(test.getTaskName())){
                testPanels.get(test.getTaskName()).setVisible(true);
            }else{
                addTest(test);
            }
        }
    }

    @Override
    public void addTest(TaskDataDto test) {
        WorkloadTaskDataService.Async.getInstance().getWorkloadTaskData(session.getSessionId(), test.getTaskName(), new AsyncCallback<WorkloadTaskDataDto>() {
            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(WorkloadTaskDataDto result) {
                TestPanel test = new TestPanel(result);
                testPanel.add(test);
                testPanels.put(result.getName(), test);
            }
        });
    }

    @Override
    public void removeTest(TaskDataDto test) {
        if (testPanels.containsKey(test.getTaskName())){
            TestPanel testPanel = testPanels.get(test.getTaskName());
            if (testPanel.isVisible()){
                testPanel.setVisible(false);
            }
        }
    }

    @Override
    public void showMetric(TaskDataDto test, String metricName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void hideMetric(TaskDataDto test, String metricName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void showMetric(String metricName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void hideMetric(String metricName) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
