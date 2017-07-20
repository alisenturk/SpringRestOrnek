package com.alisenturk.dao.kullanici;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.alisenturk.config.ConnectionPool;
import com.alisenturk.config.Constants;
import com.alisenturk.exceptions.HBRuntimeException;
import com.alisenturk.model.base.AramaKriter;
import com.alisenturk.model.base.HashFields;
import com.alisenturk.model.base.ProcessLog;
import com.alisenturk.model.kullanici.Kullanici;
import com.alisenturk.model.kullanici.Role;
import com.alisenturk.model.parametre.Departman;
import com.alisenturk.model.response.ResponseData;
import com.alisenturk.model.response.ResponseStatus;
import com.alisenturk.util.HashGenerator;
import com.alisenturk.util.Helper;

@Repository
@Scope(scopeName="request",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class KullaniciDAO implements IKullaniciDAO {

	private static final long serialVersionUID = -4936769992401381205L;
	
	@Autowired
	ConnectionPool	dataSource;
	
	/**
	 * @return Kullanıcı adına göre personel araması yapar. Eğer kullanıcının IMECE.KULLANICI_STATU tablosunda kaydı var ise
	 * tablodan statu statusu ve buna bağlı bilgiler alınır ve pojoya setlenir.
	 * Eğer kullanıcının tabloda kaydı yoksa(Daha önce uygulama üzerinden hiç durum deiştirmemişse) statusu pojoya statik
	 * olarak "Uygun" setlenir. 
	 */
	@Cacheable(value="kullaniciCache",key="#kullaniciAdi",unless="#result==null")
	public Kullanici araKullanici(String kullaniciAdi) {
		Kullanici 		kull 	= null;
		StringBuilder 	sql 	= new StringBuilder(); 
		PreparedStatement 		st 	= null;
		ResultSet 				rs 	= null;
		Connection 				conn= null;
		try{
			
			
			conn = dataSource.getConnection();
			st = conn.prepareStatement(sql.toString());
			
			
			rs = st.executeQuery();
			HashGenerator<Kullanici> kullHashGen = new HashGenerator<>();
			while(rs.next()){
				kull = new Kullanici();
				kull.setKullaniciAdi(kullaniciAdi);
				kull.setPersonelNo(rs.getInt("P_NO"));
				kull.setAd(Helper.checkNulls(rs.getString("P_AD"),""));
				kull.setSoyad(Helper.checkNulls(rs.getString("P_SOYAD"),""));
				kull.setMiskod(Helper.checkNulls(rs.getString("V_MISKOD"),""));
				if(kull.getMiskod().length()<1){
					kull.setMiskod(Helper.checkNulls(rs.getString("MISKOD"),""));
				}
				kull.setEmail(Helper.checkNulls(rs.getString("P_EMAIL1"),""));
				kull.setDepartman(new Departman(Helper.checkNulls(rs.getString("P_DPT"),""),Helper.checkNulls(rs.getString("DP_AD"),"")));
			}
			rs.close();
			st.close();
			
			if(kull!=null)
				kull.getRoles().addAll(getKullaniciRolleri(kull.getPersonelNo())); //Kullanıcı roleleri yükleniyor
			
		}catch(RuntimeException | SQLException e){
			kull = null;
			Helper.errorLogger(getClass(), e,"[SQL]..:" + sql.toString());
		} catch (Exception e) {
			Helper.errorLogger(getClass(), e,"[SQL]..:" + sql.toString());
		}finally{
			dataSource.closeConnection(conn);
			dataSource.closeResultSet(rs);
			dataSource.closeStatement(st);			
		}
		return kull;
	}
	
	
	/**
	 * @return Kullanıcı adına göre personel araması yapar
	 */
	@Cacheable(value="kullaniciCache",key="#token",unless="#result==null")
	@Override
	public Kullanici araKullaniciToken(String token) {
		Kullanici 		kull 	= null;
		StringBuilder 	sql 	= new StringBuilder(); 
		PreparedStatement 	pst = null;
		ResultSet 			rs	= null;
		Connection			conn=null;
		try{
			sql.append("SELECT ");
			sql.append("	PP.P_NO, ");
			sql.append("	PP.P_AD, ");
			sql.append("	PP.P_SOYAD, ");
			sql.append("	PP.P_UNVAN, ");
			sql.append("	PP.UNVAN_AD, ");
			sql.append("	PP.P_GOREV, ");
			sql.append("	PP.PZ_AD, ");
			sql.append("	PM.MISKOD, ");
			sql.append("	PM.V_MISKOD, ");
			sql.append("	PP.P_DPT, ");
			sql.append("	PP.DP_AD, ");
			sql.append("	PP.P_EMAIL1, ");
			sql.append("	PP.P_CEPTEL, ");
			sql.append("	bg.BOLGE_KODU,");
			sql.append("	bg.BOLGE_ADI,	");
			sql.append("	it.kullanici_adi, ");
			sql.append("	tbg.BOLGE_KODU token_bolge_kodu, ");
			sql.append("	tbg.BOLGE_ADI token_bolge_adi,	");
			sql.append("	st.* ");			
			sql.append("FROM  ");
			sql.append("	IMECE.IMECE_TOKEN it  ");
			sql.append("	JOIN DB2ADMIN.POR_PERSONEL pp ON pp.P_NO = it.SICIL ");
			sql.append("	JOIN DB2ADMIN.por_mistral pm ON pm.p_no = pp.P_NO ");
			sql.append("	JOIN IMECE.BOLGE tbg ON it.bolge = tbg.bolge_kodu ");
			sql.append("	LEFT OUTER JOIN IMECE.KULLANICI_STATU ST ON pp.p_no = ST.PERSONEL_NO AND (ST.DURUM = 'AKTIF' OR ST.DURUM IS NULL) ");
			sql.append("	LEFT OUTER JOIN IMECE.BOLGE bg ON pp.DP_KOD = bg.DP_KOD ");
			sql.append("WHERE  ");
			sql.append("	it.TOKEN = ? ");
			sql.append("	AND it.DURUM = 'AKTIF' ");
			sql.append(Constants.DB2_READ_ONLY_QUERY);
			conn = dataSource.getConnection();
			pst = conn.prepareStatement(sql.toString());
			pst.setString(1,token);
			
			rs = pst.executeQuery();
			HashGenerator<Kullanici> kullHashGen = new HashGenerator<>();
			while(rs.next()){
				kull = new Kullanici();
				kull.setKullaniciAdi(rs.getString("kullanici_adi"));
				kull.setPersonelNo(rs.getInt("P_NO"));
				kull.setAd(Helper.checkNulls(rs.getString("P_AD"),""));
				kull.setSoyad(Helper.checkNulls(rs.getString("P_SOYAD"),""));
				kull.setMiskod(Helper.checkNulls(rs.getString("V_MISKOD"),""));
				if(kull.getMiskod().length()<1){
					kull.setMiskod(Helper.checkNulls(rs.getString("MISKOD"),""));
				}
				kull.setEmail(Helper.checkNulls(rs.getString("P_EMAIL1"),""));
				kull.setDepartman(new Departman(Helper.checkNulls(rs.getString("P_DPT"),""),Helper.checkNulls(rs.getString("DP_AD"),"")));
				kull.generateHashValue();
			}
			rs.close();
			pst.close();
			if(kull!=null)
				kull.getRoles().addAll(getKullaniciRolleri(kull.getPersonelNo())); //Kullanıcı roleleri yükleniyor
			
		}catch(RuntimeException |  SQLException e){
			kull = null;
			Helper.errorLogger(getClass(), e,"[SQL]..:" + sql.toString());
		}finally{
			dataSource.closeConnection(conn);
			try{
				if(pst!=null)pst.close();
			}catch (SQLException e){
				Helper.errorLogger(getClass(), e);
			}
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {Helper.errorLogger(getClass(), e);}
		}
		return kull;
	}
	
	@Cacheable(value="kullaniciRoleCache",key="#personelNo",unless="#result.size()==0")
	public List<Role> getKullaniciRolleri(long personelNo){
		List<Role> roles = new ArrayList<>();
		Connection 			conn 	= null;
		PreparedStatement	ps		= null;
		ResultSet			rs		= null;
		StringBuilder		sql		= null;
		CallableStatement myCallableStatement = null;
		try{
			sql = new StringBuilder();
			
				
			
			conn = dataSource.getConnection();
			myCallableStatement = (CallableStatement) conn.prepareCall(sql.toString());
			rs = myCallableStatement.executeQuery();
			
			while(rs.next()){
				roles.add(new Role(rs.getLong("ID"),rs.getString("CODE"),rs.getString("NAME")));
			}			

		}catch(HBRuntimeException | NullPointerException | SQLException e){
			Helper.errorLogger(getClass(), e,"[SQL]..:" + (null!=sql?sql.toString():"") + " [UserID]..:" + personelNo);
		}finally{
			dataSource.closeConnection(conn);
			dataSource.closeResultSet(rs);
			dataSource.closeStatement(ps);
			dataSource.closeStatement(myCallableStatement);
		}
		
		return roles;
	}

	@Override
	@Cacheable(value="kullaniciListCache",key="#kriter",unless="#result.size()==0")
	public List<Kullanici> kullaniciListesi(AramaKriter kriter,int precordCount) {
		
		List<Kullanici> list	= new ArrayList<>();
		Kullanici 		kull 	= null;
		StringBuilder 	sql 	= new StringBuilder(); 
		PreparedStatement 		st 	= null;
		ResultSet 				rs 	= null;
		Connection 				conn= null;
		try{
			int recordCount = precordCount;
			if(recordCount<1 || recordCount>100){
				recordCount = 10;
			}
			if(kriter.getQuery()==null || kriter.getQuery().length()<3){
				throw new HBRuntimeException("Query en az 3 karakter olmalı");
			}
			String[] params = null;
			
			params = kriter.getQuery().split(" ",2);
			
			sql.append("SELECT  ");
			sql.append("	PP.P_NO, "); 
			sql.append("	PP.P_AD,  ");
			sql.append("	PP.P_SOYAD,  ");
			sql.append("	PP.P_UNVAN,  ");
			sql.append("	PP.UNVAN_AD,  ");
			sql.append("	PP.P_GOREV,  ");
			sql.append("	PP.PZ_AD,  ");
			sql.append("	pm.MISKOD,  ");
			sql.append("	pm.V_MISKOD,  ");
			sql.append("	PP.P_DPT,  ");
			sql.append("	PP.DP_AD,  ");
			sql.append("	PP.P_EMAIL1,  ");
			sql.append("	PP.P_CEPTEL,  ");
			sql.append("	PP.RESIM_ADRES,  ");
			sql.append("	bg.BOLGE_KODU, ");
			sql.append("	bg.BOLGE_ADI,	 ");
			sql.append("	'USER' TIP, ");
			sql.append("	(select count(1) from imece.imece_token it where it.sicil = pp.p_no and it.durum='AKTIF') loggedin, ");
			sql.append(" 	vbg.bolge_kodu vekil_bgkodu, ");
			sql.append("	vbg.bolge_adi vekil_bgadi ");
			sql.append("FROM DB2ADMIN.POR_PERSONEL pp "); 
			sql.append("	JOIN DB2ADMIN.por_mistral pm ON pm.p_no = pp.P_NO "); 
			sql.append("	LEFT OUTER JOIN IMECE.BOLGE bg ON pp.DP_KOD = bg.DP_KOD ");
			sql.append("	LEFT OUTER JOIN IMECE.BOLGE vbg ON pm.v_dpt = vbg.DP_KOD ");
			sql.append("WHERE ");
			sql.append("	(");
			sql.append("	pp.P_NO = DB2ADMIN.FIND_PERSONEL_ID(?) ");
			sql.append("	OR ");
			sql.append("	pp.AD_SOYAD like ? ");
			sql.append("	) ");
			if(params.length>1){
				sql.append("	AND ");
				sql.append("	pp.AD_SOYAD like  ? ");			
			}
			if(kriter.getType()!=null && "SATISTEMSILCISI".equalsIgnoreCase(kriter.getType())){
				sql.append(" AND ");
				sql.append("  trim(pp.p_gorev) like '%BST' ");
			}
			
			
			sql.append("  FETCH FIRST "+ recordCount +" ROWS ONLY ");
			sql.append(Constants.DB2_READ_ONLY_QUERY); 
			
			conn = dataSource.getConnection();
			st = conn.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			st.setString(1,params[0].toUpperCase());
			st.setString(2,"%"+params[0].toUpperCase()+"%");
			if(params.length>1){
				st.setString(3,"%"+params[1].toUpperCase()+"%");
			}
			
			rs = st.executeQuery();
			HashGenerator<Kullanici> kullHashGen = new HashGenerator<>();
			int rowCount =  Helper.getRowCount(rs);
			while(rs.next()){
				kull = new Kullanici();
				kull.setPersonelNo(rs.getInt("P_NO"));
				kull.setAd(Helper.checkNulls(rs.getString("P_AD"),""));
				kull.setSoyad(Helper.checkNulls(rs.getString("P_SOYAD"),""));
				kull.setMiskod(Helper.checkNulls(rs.getString("V_MISKOD"),""));
				if(kull.getMiskod().length()<1){
					kull.setMiskod(Helper.checkNulls(rs.getString("MISKOD"),""));
				}
				kull.setEmail(Helper.checkNulls(rs.getString("P_EMAIL1"),""));
				kull.setDepartman(new Departman(Helper.checkNulls(rs.getString("P_DPT"),""),Helper.checkNulls(rs.getString("DP_AD"),"")));
								
				kull.setRecType(Helper.checkNulls(rs.getString("TIP"),"USER"));
				kull.setLoggedIn(rs.getInt("loggedin")>0);
				
				kull.generateHashValue();
				list.add(kull);
			}
			rs.close();
			st.close();
			
			
		}catch(RuntimeException | SQLException e){
			kull = null;
			Helper.errorLogger(getClass(), e,"[SQL]..:" + sql.toString());
		} catch (Exception e) {
			Helper.errorLogger(getClass(), e,"[SQL]..:" + sql.toString());
		}finally{
			dataSource.closeConnection(conn);
			dataSource.closeResultSet(rs);
			dataSource.closeStatement(st);			
		}
		return list;
	}
	


	@Override
	public ResponseData<String> makePassiveToToken(String token) {
		ResponseData<String> responseData 	= new ResponseData<>();
		Connection con 						= null;
		PreparedStatement pst 				= null;
		String query 						= null;
		try {
			query = "UPDATE IMECE.IMECE_TOKEN SET DURUM = ? WHERE TOKEN = ?";
			con = this.dataSource.getConnection();
			pst = con.prepareStatement(query);
			pst.setString(1, "PASIF");
			pst.setString(2, token);
			pst.executeUpdate();
			
			responseData.setData("OK");
			responseData.setStatusCode(ResponseStatus.OK.getCode());
			responseData.setStatusMessage("");
			
		} catch (SQLException sqlEx) {
			responseData.setData("NOK");
			responseData.setStatusCode(ResponseStatus.NOK.getCode());
			responseData.setStatusMessage(""); //hata alsakta gostermemek için "" setliyoruz
			Helper.errorLogger(getClass(), sqlEx);
		} finally{
			this.dataSource.closeConnection(con);
			this.dataSource.closeStatement(pst);
		}
		
		return responseData;
	}

	

}
