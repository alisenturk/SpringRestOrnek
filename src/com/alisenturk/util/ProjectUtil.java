package com.alisenturk.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectUtil {

	private ProjectUtil(){
		super();
	}
	
	
	public static List<String> getTaskRunnerList(){
		List<String> list = new ArrayList<>();
		String str = Helper.getAppMessage("scheduler.task.runner.list");
		list.addAll(Arrays.asList(str.split(",")));
		return list;
	}
	
	
	
	
	
	
	public static boolean havePermissionRunForTask(String ipAddress){
		return ProjectUtil.getTaskRunnerList().contains(ipAddress);
	}
	
	
}
