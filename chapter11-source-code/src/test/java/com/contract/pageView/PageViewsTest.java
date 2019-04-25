package com.contract.pageView;

import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;

/**
 * @author zzs
 */
public class PageViewsTest {

	/**
	 * 智能合约地址
	 */
	private static final String CONTRACT_ADDRESS = "0x58b7ce68bb8cc47841d1ff9646b994e5fa250b02";

	/**
	 * 调用geth节点地址
	 */
	private static final String URL = "http://localhost:8545";

	@Test
	public void testContract() throws Exception {
		// 构建请求geth节点的链接，由于要操作交易记录，因此需要使用admin对象，如果仅仅查询则使用Web3j或Geth对象即可。
		Admin admin = Admin.build(new HttpService(URL, false));
		Credentials credentials = null;
		try {
			// 加载私钥等相关信息，其中第一个参数为私钥密码，第二个参数为私钥存储的文件路径
			credentials = WalletUtils.loadCredentials("",
					"/Users/zzs/my/book/geth1.8/geth/data/keystore/UTC--2018-06-11T15-00-05.188042543Z--da652ca84f531deb3c2baeacfda3a2b00aa01c23");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 初始智能合约对象，其中后两个参数分别为发起交易对应的gasPrice和gasLimit。
		PageViews pageViews = PageViews.load(CONTRACT_ADDRESS, admin, credentials, new BigInteger("21000"), new BigInteger("90000"));

		// 获得访问请求方法
		RemoteCall<TransactionReceipt> call = pageViews.visit();
		// 发送请求到智能合约，并获得结果
		TransactionReceipt receipt = call.send();
		// 打印交易结果对应的hash值
		System.out.println(receipt.getBlockHash());

		// 获得智能合约中对应获取访问量的方法
		RemoteCall<BigInteger> viewsCall = pageViews.getViews();
		// 发送请求到智能合约，并获得结果
		BigInteger result = viewsCall.send();
		// 打印目前访问量
		System.out.println(result);

	}
}
