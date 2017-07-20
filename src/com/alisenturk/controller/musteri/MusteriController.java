package com.alisenturk.controller.musteri;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alisenturk.annotations.Loggable;
import com.alisenturk.annotations.TokenControl;
import com.alisenturk.controller.BaseController;
import com.alisenturk.dao.base.ICacheManager;
import com.alisenturk.dao.musteri.IMusteriDAO;
import com.alisenturk.exceptions.ExceptionMessage;
import com.alisenturk.exceptions.HBRuntimeException;
import com.alisenturk.model.base.HashFields;
import com.alisenturk.model.base.Status;
import com.alisenturk.model.kullanici.Kullanici;
import com.alisenturk.model.musteri.Musteri;
import com.alisenturk.model.musteri.MusteriNotu;
import com.alisenturk.model.response.ResponseData;
import com.alisenturk.model.response.ResponseStatus;
import com.alisenturk.util.HashGenerator;
import com.alisenturk.util.Helper;




@RestController
@RequestMapping("/musteri")
//@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MusteriController implements BaseController {
	
	@Autowired
	IMusteriDAO musteriDAO;
	
	
	@Autowired
	ICacheManager cacheManager;
	
	@RequestMapping(value = "/ara", method = RequestMethod.POST, produces = "application/json")
	@TokenControl(kullaniciDogrulamasi=true)
	@Loggable(groupCode="MUSTERI", methodDesc="Müşteri Arama",arguments={"kriter","token","enlem","boylam"},
			  iconClass="glyphicon glyphicon-user",viewTimeline=true,detail={"kriter"})
	@ResponseBody
	public ResponseData<List<Musteri>> musteriAra(	@RequestBody MusteriAramaKriterleri kriter,
													@RequestHeader(name="hbtoken",defaultValue="bos") String token,
													@RequestHeader(name="hbenlem",defaultValue="-1") String enlem,
													@RequestHeader(name="hbboylam",defaultValue="-1") String boylam,
													Kullanici kullanici
												 ){
		ResponseData<List<Musteri>> response = new ResponseData<>();
		try{
			
			response = musteriDAO.musteriAraList(kriter.getKullanici(),kriter);
			if(!response.getData().isEmpty()){
				response.setStatusCode(ResponseStatus.OK.getCode());
				response.setStatusMessage("");
			}else{				
				response.setStatusCode(ResponseStatus.NORECORD.getCode());
				response.setStatusMessage(response.getStatusMessage());
			}
			/*** Timeline'da gösterilecek metodlar için cache temizlemesi yapılmalı **/
			cacheManager.clearCache("", "kullaniciAktivite");
		}catch(Exception e){
			response.setStatusCode(ResponseStatus.NORECORD.getCode());
			response.setStatusMessage(ResponseStatus.NORECORD.getMessage());
			Helper.errorLogger(getClass(), e);
		}
		
		return response;
	}
	@RequestMapping(value = "/notlari/giris", method = RequestMethod.POST, produces = "application/json")
	@TokenControl(kullaniciDogrulamasi=true)
	@Loggable(groupCode="MUSTERI",methodDesc="Müşteri Ziyaret Notu Girişi",arguments={"not","token","enlem","boylam"},
			 iconClass="glyphicon glyphicon-user",viewTimeline=true,
			 detail={"not"," ziyaret notu girişi yapılmıştır."} )
	@ResponseBody
	public ResponseData<String> musteriNotlariGiris(	@RequestBody MusteriNotu not,
														@RequestHeader(name="hbtoken",defaultValue="bos") String token,
												  	  	@RequestHeader(name="hbenlem",defaultValue="-1") String enlem,
												  	  	@RequestHeader(name="hbboylam",defaultValue="-1") String boylam){
		ResponseData<String> response = new ResponseData<String>();
		try{
			
			if(not==null || not.getMusteriNo()<=0){
				throw new HBRuntimeException(ExceptionMessage.MUSTERI_BOS_OLAMAZ);
			}
			if(not.getValueHash()==null){
				throw new HBRuntimeException("ValueHash değeri boş olamaz!");
			}
			
			HashGenerator<Musteri> hashGen = new HashGenerator<>();
			
			Musteri musteri = new Musteri();
			musteri.setMusteriNo(not.getMusteriNo());
			String newHash = hashGen.generateHash(musteri, HashFields.MUSTERI.getHashFields());
			if(!newHash.equals(not.getValueHash().trim())){				
				throw new HBRuntimeException("Uyumsuz hash değeri!");
			}
			not.setNotTipKod(MusteriNotu.NOT_TIP_ZIYARET);
			//not.setOperatorId(getKullanici().getKullaniciAdi());
			not.setStatus(Status.AKTIF);
			//response = ziyaretNotDAO.saveMusteriNotu(not);
			
			if(response.getStatusCode().equals(ResponseStatus.OK.getCode())){
				
				response.setStatusCode(ResponseStatus.OK.getCode());
				response.setStatusMessage("");
				cacheManager.clearCache("", "musteriNotlariCache");
				cacheManager.clearCache("", "musteriNotlariGrupCache");
			}else{
				response.setStatusCode(ResponseStatus.NORECORD.getCode());
				response.setStatusMessage(ResponseStatus.NORECORD.getMessage());
			}
			/*** Timeline'da gösterilecek metodlar için cache temizlemesi yapılmalı **/
			cacheManager.clearCache("", "kullaniciAktivite");
		}catch(HBRuntimeException e){
			response.setStatusCode(ResponseStatus.NOK.getCode());
			response.setStatusMessage(e.getMessage());
			Helper.errorLogger(getClass(), e);
		}
		
		return response;
	}
	
	
}
