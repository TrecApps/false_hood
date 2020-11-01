package com.trecapps.false_hood.test_obj;

/**
 * 
 * @author Lord Tormontrec
 *
 * @apiNote The test key is a key created explicitly for testing and should not be used for production
 * Also, the "MainEmail" embedded in the tokens are not real emails (to my knowledge)
 */
public class UserTokens {

	public static final String testKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAxRTKST6jgcrze+5weDAXMpDYlgMT8oF"
			+ "hY0ObLIJTrxLmiqW3+quLvtYdOkE8dMQFbyskPmtpVQ4ZzylNBTaNigYM4THpDwBGGUnsaHJAcTSq72HToB0iAn4TPP09pSpdSFhaM"
			+ "/UNXp+rHUjVkiCYwQKRtrISBJwmTWaZ1SgYkOfcCu44Lto4WNjIhyOF3icGdYFIZBkmAFGAieBJ26j3WsDy7ED1xCS08gLSoez1GdG"
			+ "YixZc4AwIBxlu1r4XaveDZSS7xgQ/cpg++kP7r3Kkb9n8rDyNuynngYUU2w4IGTuL2/OgZaghqGo2HJH1KDdymYXnb9YnVxC3loyDT"
			+ "eDaRbOZEJFGNijGduMtuvuttb5v1qi631h2wUYAi2YOz7xktcSVpBYI92UzE2f7zOoU5lJY4j1QZPd8aFRRAgzJNFaPq3wEwpdkqB/"
			+ "LF/sw7V2y9PjGULyLM8ErzLtNM/dpKqCyQaAa7YyCUBssa4vXKeOOAqTEoDjs5DbGzAyYUclGvyQpSHzQ04L+W8RuMk3rawZgqZvZU"
			+ "9E439VAnd4N4xqvKjBU7ZolTFFd0d5pJX8KKAcy71fG/L18t2nMlcuM2xxtcu5CpQvddbP6HuiPhg9EOJr7NjvAsV92VkSuNlZI2Ah"
			+ "vYj1zTjODLlI4nthP2CwodqrXRYjQ8Bh9NN3ycYMCAwEAAQ==";
	
	/**
	 * This token can be unlocked by the public key above, generated specifically for testing purposes and should
	 * Not be used in Production
	 * 
	 * ID = 1,
	 * Validated = true
	 */
	public static final String userToken1 = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJWYWxpZGF0ZWQiOnRydWUsIklEIjoxLCJN"
			+"YWluRW1haWwiOiJsb3JkLnRvcm1vbnRyZWNAZ21haWwuY29tIiwiaWF0IjoxNTE2MjM5MDIyfQ.h8pgchQTZui2HkJT0IWKBFxXisc4P"
			+"UxetZB-mBhMWlsMJDppfWPCxRe73ZzwL922n15XEwka9dIG5fzgv3ia0Ta6xoE-mSD-4uecGaXrh604r8mWhGuzBfnyjljGylk_dPilR"
			+"VwBc1NYc_hhlT4d682PnIzQxE8u_raDiIqLlSk7ekc31FD-fTRi9zd54bqiPkxYKhG2EgB_joLnky7QImknwsmg4OIxvvjQK_MUunvOX"
			+"sG3EPGP6YkUkwbKZpb38WIOOrYZgNhSCHeQUqQ0GItULbqn4-vGE7eZuP7LniG5lFqHkUWciOOJAU02kyO8sybEsW0ySl481KLnFNHAf"
			+"CCHc3ku3FNt-ewpIbvXo3uYaGwiQtQf-PNZKstpPrhT9HMgXxw6TLdYojHtgz8vbmLE_c6VG8iqcuKv2XHwxBrfOFnSKyjinV_T8G9Sa"
			+"lWOBc92MLZjGWLcUeiXL-Gph7FwwDtTxlYi6_e6SaHrPQ3rrF8Af0y5goLWM-EY3r1v2iyEB9-WfDDXg4Qhh1o9e5ysek8m_qQWcVAwN"
			+"mzTHqUnP9plDfwtdgXXr9XTJfG17rvlFnvqkHkoBD_ysP5hrMyfkHXkksXZBF5HrFGOPKfVsp1DgUiErZQrZ_s1v85b2orQRkdTMwMpB"
			+"Q-HH4qWD3nxQX3r-b3DCGnwxJ4Dk3o";
	
	/**
	 * ID = 2
	 * Validated = false
	 */
	public static final String userToken2 = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJWYWxpZGF0ZWQiOmZhbHNlLCJJRCI6Miwi"
			+"TWFpbkVtYWlsIjoibG9yZC50b3Jtb250cmVjQGdtYWlsLmNvbSIsImlhdCI6MTUxNjIzOTAyMn0.IwnkxQ_0NYS36F2qnBUeZsPvXQnJ"
			+"E7arcfuNmV6-NQacoDL5Nc0Tu4KO2FqkZqvfzTnhqIcvhXjZVUFHSm9uiyZuyPB1DS-FIdJ9SXBlb-cQ_tq9MTmSo6exLX9DEfxCjruH"
			+"1RP-YnCF_jeQ7Jtfs3kbqLGA9nVRYtvMmbhVCX72waF32e8q91aOPBXR0-9WLcfDyEZ6XYWm3LJ16jfUCNnuiFFyDn78AvxG6d8bd9eO"
			+"BFkjmB-fOV66JIh2S7ycMzn_9b6e-T6lE2XENkEunpV8YiJFZzxjrIO6cXT8hDD0F6LIUwA1NFNtVTBSnuV8qYBZ_7vZx6wajNMlaDl1"
			+"vuIeMmYwUlWOWSZUuieARZ2aq6vQCDEfTK5i0l452ixYikA77inLzYd81Obec7FI1h-KqV1RV52yuYM_Pb29Jrd_pytW_hEVLKtL7VsZ"
			+"42gCD8Niv3F5_CWrgFRroL7YBQ1sYgmGBEwPmeWYHJV-9gii3uBcY53tVzRC-1G7d3tfzNZ-XYN5F1P_WWt8jY-GdeaiM_SKVwaAN5XK"
			+"cIHaGehjtuNdSu_uWt1SgnwTt4_0-Z1C2Y4aWZZh3M34_3dFGVgH_ktEJ8bqfTIKToppmFXrkL_ZlcaIRf3cssWCB4_bfvBtz-af3Zuy"
			+"fAu2OcA13nzm9oi2q3yFMo1dsBTAbfI";
	
	/**
	 * ID = 3
	 * Validated = true
	 * Different email than ID = 1
	 */
	public static final String userToken3 = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJWYWxpZGF0ZWQiOnRydWUsIklEIjozLCJN"
			+"YWluRW1haWwiOiJ0b3Jtb250cmVjQGdtYWlsLmNvbSIsImlhdCI6MTUxNjIzOTAyMn0.SJlAcOkOHiackMs0v6htmkldA5G7Tn9FMJrQ"
			+"oArj-dY6wVKGQWghWWEU8poW3Q6uxMo8f57bFImdYYJEAjA9Tn9UbZSlbD6nS1QvM7W1VJ9WAz9cWjqyaAjc4QeaD3ApLvZJgTPKp_QY"
			+"IR_Cs-RivrX-eKnRXTrnNL8uR-ckqWjzfa1mjfiUNn8aGIsnVnTSmCV_U6IVTT3jeT6By195_C0Gs2AFpJgKx0gE9Ui9eAAOiUwqAwkf"
			+"8-x5tGjUdEWQ5DZLfsvDKd8AsGwiWBCB88ixlrLnNB-IMOeaezFM5cNhuF89k1IBm533Ui7CN4859qRTtML9nI4OGg3EilqJ-fju67TE"
			+"WdfNwLmQRias5MGguE_ac0k2l21VoK86dPkt0dYlh6WOxt-7V3BqlUT_b5V-Kpvntuo31XLW_EtS4HjNZltXFZhrcYzyr2E1JeChAXfz"
			+"ZPx-6WSepam4ph3vy-092V4KwOlPb7sPx5762c2XLa2otL7fMSMl_YKpyt2L3a98ZgLd5z7OVtS43bnAsY5ZMIBTg9E0uZe2hqgcJAlj"
			+"IPiZzgL829EjipopVqcUBsBgDEn07hhZSll18sSo3sJnAQlgFhfpbw_Gpom2oZRj91imnJs_Fq4stmxeD2B_iqHy33DX_gHM5soRNkCu"
			+"6DCjcA3fmU0hIxJLDZhL1QU";
	
	/**
	 * ID = 4
	 * Validated = true
	 * Note: contains field indicating that expiration date has passed
	 */
	public static final String userToken4 = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJWYWxpZGF0ZWQiOnRydWUsIklEIjo0LCJN"
			+ "YWluRW1haWwiOiJ0b3Jtb250cmVjQGdtYWlsLmNvbSIsImV4cCI6MTUxNjI0OTAyMn0.A6oQEzz54SbjUH0KgHMtCuxsoyC80J8jWOH"
			+"bkzcTq1nceIz6yaIkSO49nIU4QIDNVFEWW7KklfHrgkQpKBJCtjOEX_zQCcOKZsv0vWxxTG175m67abNer-daXtgolCdiB0u6jmm-lhQ"
			+"oRIsHgcQd89l-ffSZ3Dh-NT-QG1yo9QcN16STcyE6XhEKyY3tBPr2kYUzdF_1u5blGfQQH9_OOBqJNaTkX6uwYn1uc0nxmYMC3Hzmben"
			+"JIYw3TftvJpiKl7pqQDabhrdwLbJjWCKqw_G8OIAaX1IbeTdAmV3TzYq4-Zb-CoXloxfJ5S8CvGdQ8IBSvNNM9XJgCiGvUP73clfSfx0"
			+"YtBHNKoc3WKqG5u30UtozmZrxCUlYK8ZwB_F0Q90cIHPYH4o1cf6Y4Dg_0bQguOOYv0yAG7qT7AbGa-5Zv7WdOiCOgD7H5OoDXoLK1fe"
			+"7Wnb-e1LfvUBQvQj1R7Kukktrot5crO_NfYQh5VKXi2gjWU8alWhQQBQPrXUl6z4RWgSa2dWesM1Cd6dNadx1uK8-KH4vn4t1Y4sR3QM"
			+"-_tYG668vSt6kq-tUZYXIdcfRRvb3XIETNRFWFzeuN-FHgMSshEf0b62i-UYMiF2H0rQ3VkVNiz1DmCw7mXbXpvkUFdzxsTNB-UNxk4P"
			+"NNcv5R8zzvMyvE19ZhKPcPr8";
}
