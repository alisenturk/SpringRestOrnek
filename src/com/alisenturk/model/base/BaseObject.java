package com.alisenturk.model.base;

import java.io.Serializable;

public interface BaseObject extends Serializable {
	
	/**
	 * ProcessLog tablosuna atılan kaydın açıklama bilgisini döndürür.
	 * @return
	 */
	public String 	getObjectDetail();
	
	/**
	 * Objenin değişmediğini anlamak için kullanılan ve JWT ile oluşturulan hash değeri
	 * @return
	 */
	public String	getObjectKey();
	
	/**
	 * ProcessLog tablosuna atılan kaydın herkes tarafından görülüp görülmeyeceğini belirtir.
	 * @return true / false döner
	 */
	public boolean  isViewEveryone();
}
