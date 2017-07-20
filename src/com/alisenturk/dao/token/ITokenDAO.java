package com.alisenturk.dao.token;

import java.util.List;

public interface ITokenDAO {
	
	public String tokenYarat(int personelNo,String kullaniciAdi,String latitude,String longitude);
	
	public boolean tokenGecerliMi(String token);
	public boolean tokenSil(String token);
	public void extendTokenValidity(String token);
	public void extendTokenValidity(List<String> tokens);
	
	/**
	 * Geriye int tipinde bir dizi döner. 
	 * Dizinin 0.nci elemanı token'ın geçerli olup olmadığını söylerken,
	 * 1.nci elemanı ise token'ın geçerlilik süresinin dolmasına kaç saniye kaldığını söyler
	 * @param token
	 * @return
	 */
	public int[] tokenGecerliMiWithTime(String token);
}
