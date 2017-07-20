package com.alisenturk.model.response;

public enum ResponseStatus {
	OK("OK","İşlem başarılı"),	
	NOK("NOK","İşlem başarısız"),
	NORECORD("NORECORD","Kayıt bulunamadı!"),
	REVOKED("REVOKED","İptal edilmiş."),
	EXPIRED("EXPIRED","Süresi dolmuş"),
	INVALID_TOKEN("INVALID_TOKEN","Geçersiz token!"),
	WRONGPASSWORD("WRONGPASSWORD","Kullanıcı adı veya şifre hatalı!"),
	SQLERROR("SQLERROR", "Sql hatası"),
	BLOCK("BLOCK","Hatalı giriş denemeleri sebebiyle kullanıcınız geçici süre blocklanmıştır!"),
	UNAUTHORIZED("UNAUTHORIZED","Bu işlem için yetkiniz bulunmamaktadır!"),
	VALIDATIONERROR("VALIDATIONERROR", "Validasyon hatası"),
	OK_AND_MORE("OK_AND_MORE","Kayıt bulundu. Ancak daha fazlası var");
	
	ResponseStatus(String value, String message) {
		this.code = value;
		this.message = message;
	}
	private String code;
	private String message;
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
}
