package com.alisenturk.util.json;

import java.io.IOException;

import com.alisenturk.util.Helper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class EnumDeserializer<E> extends JsonDeserializer<E> {
	@SuppressWarnings("unchecked")
	@Override
	public E deserialize(JsonParser jsonparser,DeserializationContext deserializationcontext) throws IOException {
		final String jsonValue = jsonparser.getText();
		
		String attrName = jsonparser.getCurrentName();
//		System.out.println("attrName..:" + attrName + " jsonValue..:" + jsonValue);
		attrName = Helper.checkNulls(attrName,"");
//		if(attrName.equalsIgnoreCase("hizSorgulamaTipi")){
//			return (E) HizmetSorgulamaTipi.getEnumByValue(Integer.parseInt(jsonValue));
//		}else if(attrName.equalsIgnoreCase("odemeTuru")){
//			return (E) TalimatOdemeTuru.getEnumByValue(Integer.parseInt(jsonValue));
//		}else if(attrName.equalsIgnoreCase("kurumTuru")){
//			return (E) KurumTurleri.getEnumByValue(Integer.parseInt(jsonValue));
//		}else if(attrName.equalsIgnoreCase("islKod")){ 
//			return (E) LKSIslemKodlari.getEnumByValue(jsonValue);
//		}else if(attrName.equalsIgnoreCase("statu")){ 
//			return (E) Statu.getEnumByValue(jsonValue);
//		}else if(attrName.equalsIgnoreCase("oneriTipi")){ 
//			return (E) OneriTipleri.getEnumByValue(jsonValue);
//		}else if(attrName.equalsIgnoreCase("aksiyonKodu")){ 
//			return (E) TeklifAksiyonKodlari.getEnumByValue(jsonValue);
//		}else if(attrName.equalsIgnoreCase("urunTipiKodu")){ 
//			return (E) KrediTipi.getEnumByValue(jsonValue);
//		}else if(attrName.equalsIgnoreCase("basvuruKanali")){ 
//			return (E) DagitimKanallari.getEnumByValue(jsonValue);
//		}else if(attrName.equalsIgnoreCase("krediTipi")){ 
//			return (E) KrediTipi.getEnumByValue(jsonValue);
//		}else if(attrName.equalsIgnoreCase("krediAltTipi")){
//			E e = (E) BireyselKrediTipleri.getEnumByValue(jsonValue);
//			if(e==null){
//				e = (E) KrediKartiTipleri.getEnumByValue(jsonValue);
//			}
//			if(e==null){
//				e = (E) KMHKrediTipi.getEnumByValue(jsonValue);
//			}
//			return e;
//		}else if(attrName.equalsIgnoreCase("modulAdi")){ 
//			return (E) ImeceModules.getEnumByValue(jsonValue);
//		}else if(attrName.equalsIgnoreCase("gelirTipi")){ 
//			return (E) GelirTipi.getEnumByValue(jsonValue);
//		}else if(attrName.equalsIgnoreCase("etkinlikDurum")){ 
//			return (E) EtkinlikDurum.getEnumByValue(jsonValue);
//		}else if(attrName.equalsIgnoreCase("etkinlikTipi")){ 
//			return (E) EtkinlikTipi.getEnumByValue(jsonValue);
//		}
//		else{
//			return null;
//		}
		return null;
	}
	
}

