package com.alisenturk.model.base;

public enum HashFields {
	KULLANICI(){
		public String[] getHashFields(){
			String[] fields = {"personelNo"};
			return fields;
		}
	},
	BOLGE(){
		public String[] getHashFields(){
			String[] fields = {"bolgeKodu","serialVersionUID"};
			return fields;
		}
	},
	KULLANICI_ONERI(){
		public String[] getHashFields(){
			String[] fields = {"personelNo","id","today"};
			return fields;
		}
	},
	KULLANICI_STATU(){
		public String[] getHashFields(){
			String[] fields = {"personelNo","id","today"};
			return fields;
		}
	},
	MENU(){
		public String[] getHashFields(){
			String[] fields = { "id", "today" };
			return fields;
		}
	},
	PAGE(){
		public String[] getHashFields(){
			String[] fields = {"id","today"};
			return fields;
		}
	},
	SUBE(){
		public String[] getHashFields(){
			String[] fields = {"subeKodu","bolgeKodu"};
			return fields;
		}
	},
	MUSTERI(){
		public String[] getHashFields(){
			String[] fields = {"musteriNo","tcKimlikNo"};
			return fields;
		}
	};
	
	public abstract String[] getHashFields();
}
