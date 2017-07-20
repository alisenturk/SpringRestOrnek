package com.alisenturk.dao.base;

import java.io.Serializable;
import java.util.concurrent.Future;

import com.alisenturk.model.base.ProcessLog;

public interface IProcessLogDAO extends Serializable {
	
	public void insertLog(ProcessLog log);
	public void clearLog();
	public String findUsernameByToken(String token);
	public int  getLoginErrorCount(String username);
	public void deleteLoginError();
	
	public Future<Boolean>  insertLogAsync(ProcessLog log);
}
