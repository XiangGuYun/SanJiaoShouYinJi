package android_serialport_api.cardwriteread.meal;

import java.util.List;

public class BaseActModel
{


	/**
	 * code : 200
	 * message : SUCCESS
	 * data : {"orderNo":"M1574757003296SDB","productOrderUrl":null,"serialNumber":"1574757003296","serial":1,"shopId":23,"shopName":"菜品测试食堂","cashierDeskId":1496,"customerId":12450,"customerName":"江晨","customerPhone":null,"payType":4,"totalFee":100,"realFee":100,"refundFee":0,"payStatus":1,"refundStatus":0,"note":null,"payTime":1574757003000,"refundTime":1574757003000,"itemCount":1,"orderItems":[{"productId":5097,"productName":"自定义","calorie":null,"price":1,"realPrice":1,"quantity":1,"hasInfo":false}],"nutritionList":null,"qrCode":null,"payCard":null,"smkaccountBalance":null}
	 */

	private int code;
	private String message;
	private DataBean data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public static class DataBean {
		/**
		 * orderNo : M1574757003296SDB
		 * productOrderUrl : null
		 * serialNumber : 1574757003296
		 * serial : 1
		 * shopId : 23
		 * shopName : 菜品测试食堂
		 * cashierDeskId : 1496
		 * customerId : 12450
		 * customerName : 江晨
		 * customerPhone : null
		 * payType : 4
		 * totalFee : 100
		 * realFee : 100
		 * refundFee : 0
		 * payStatus : 1
		 * refundStatus : 0
		 * note : null
		 * payTime : 1574757003000
		 * refundTime : 1574757003000
		 * itemCount : 1
		 * orderItems : [{"productId":5097,"productName":"自定义","calorie":null,"price":1,"realPrice":1,"quantity":1,"hasInfo":false}]
		 * nutritionList : null
		 * qrCode : null
		 * payCard : null
		 * smkaccountBalance : null
		 */

		private String orderNo;
		private Object productOrderUrl;
		private String serialNumber;
		private int serial;
		private int shopId;
		private String shopName;
		private int cashierDeskId;
		private int customerId;
		private String customerName;
		private String customerPhone;
		private int payType;
		private int totalFee;
		private int realFee;
		private int refundFee;
		private int payStatus;
		private int refundStatus;
		private Object note;
		private long payTime;
		private long refundTime;
		private int itemCount;
		private Object nutritionList;
		private Object qrCode;
		private PayCard payCard;
		private Object smkaccountBalance;
		private List<OrderItemsBean> orderItems;

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public Object getProductOrderUrl() {
			return productOrderUrl;
		}

		public void setProductOrderUrl(Object productOrderUrl) {
			this.productOrderUrl = productOrderUrl;
		}

		public String getSerialNumber() {
			return serialNumber;
		}

		public void setSerialNumber(String serialNumber) {
			this.serialNumber = serialNumber;
		}

		public int getSerial() {
			return serial;
		}

		public void setSerial(int serial) {
			this.serial = serial;
		}

		public int getShopId() {
			return shopId;
		}

		public void setShopId(int shopId) {
			this.shopId = shopId;
		}

		public String getShopName() {
			return shopName;
		}

		public void setShopName(String shopName) {
			this.shopName = shopName;
		}

		public int getCashierDeskId() {
			return cashierDeskId;
		}

		public void setCashierDeskId(int cashierDeskId) {
			this.cashierDeskId = cashierDeskId;
		}

		public int getCustomerId() {
			return customerId;
		}

		public void setCustomerId(int customerId) {
			this.customerId = customerId;
		}

		public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}

		public String getCustomerPhone() {
			return customerPhone;
		}

		public void setCustomerPhone(String customerPhone) {
			this.customerPhone = customerPhone;
		}

		public int getPayType() {
			return payType;
		}

		public void setPayType(int payType) {
			this.payType = payType;
		}

		public int getTotalFee() {
			return totalFee;
		}

		public void setTotalFee(int totalFee) {
			this.totalFee = totalFee;
		}

		public int getRealFee() {
			return realFee;
		}

		public void setRealFee(int realFee) {
			this.realFee = realFee;
		}

		public int getRefundFee() {
			return refundFee;
		}

		public void setRefundFee(int refundFee) {
			this.refundFee = refundFee;
		}

		public int getPayStatus() {
			return payStatus;
		}

		public void setPayStatus(int payStatus) {
			this.payStatus = payStatus;
		}

		public int getRefundStatus() {
			return refundStatus;
		}

		public void setRefundStatus(int refundStatus) {
			this.refundStatus = refundStatus;
		}

		public Object getNote() {
			return note;
		}

		public void setNote(Object note) {
			this.note = note;
		}

		public long getPayTime() {
			return payTime;
		}

		public void setPayTime(long payTime) {
			this.payTime = payTime;
		}

		public long getRefundTime() {
			return refundTime;
		}

		public void setRefundTime(long refundTime) {
			this.refundTime = refundTime;
		}

		public int getItemCount() {
			return itemCount;
		}

		public void setItemCount(int itemCount) {
			this.itemCount = itemCount;
		}

		public Object getNutritionList() {
			return nutritionList;
		}

		public void setNutritionList(Object nutritionList) {
			this.nutritionList = nutritionList;
		}

		public Object getQrCode() {
			return qrCode;
		}

		public void setQrCode(Object qrCode) {
			this.qrCode = qrCode;
		}

		public PayCard getPayCard() {
			return payCard;
		}

		public void setPayCard(PayCard payCard) {
			this.payCard = payCard;
		}

		public Object getSmkaccountBalance() {
			return smkaccountBalance;
		}

		public void setSmkaccountBalance(Object smkaccountBalance) {
			this.smkaccountBalance = smkaccountBalance;
		}

		public List<OrderItemsBean> getOrderItems() {
			return orderItems;
		}

		public void setOrderItems(List<OrderItemsBean> orderItems) {
			this.orderItems = orderItems;
		}

		public static class OrderItemsBean {
			/**
			 * productId : 5097
			 * productName : 自定义
			 * calorie : null
			 * price : 1
			 * realPrice : 1
			 * quantity : 1
			 * hasInfo : false
			 */

			private int productId;
			private String productName;
			private Object calorie;
			private int price;
			private int realPrice;
			private int quantity;
			private boolean hasInfo;

			public int getProductId() {
				return productId;
			}

			public void setProductId(int productId) {
				this.productId = productId;
			}

			public String getProductName() {
				return productName;
			}

			public void setProductName(String productName) {
				this.productName = productName;
			}

			public Object getCalorie() {
				return calorie;
			}

			public void setCalorie(Object calorie) {
				this.calorie = calorie;
			}

			public int getPrice() {
				return price;
			}

			public void setPrice(int price) {
				this.price = price;
			}

			public int getRealPrice() {
				return realPrice;
			}

			public void setRealPrice(int realPrice) {
				this.realPrice = realPrice;
			}

			public int getQuantity() {
				return quantity;
			}

			public void setQuantity(int quantity) {
				this.quantity = quantity;
			}

			public boolean isHasInfo() {
				return hasInfo;
			}

			public void setHasInfo(boolean hasInfo) {
				this.hasInfo = hasInfo;
			}
		}

		public static class PayCard{
			private Long accountbalance;

			public Long getAccountbalance() {
				return accountbalance;
			}

			public void setAccountbalance(Long accountbalance) {
				this.accountbalance = accountbalance;
			}
		}
	}
}
