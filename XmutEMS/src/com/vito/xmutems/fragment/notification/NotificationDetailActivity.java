package com.vito.xmutems.fragment.notification;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.vito.xmutems.R;
import com.vito.xmutems.domain.Notification;
import com.vito.xmutems.utils.HttpClientFactory;

public class NotificationDetailActivity extends Activity {
	TextView title;
	TextView body;
	TextView link;
	Notification notification;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_detail);
		setTitle(getString(R.string.notification));
		title = (TextView) findViewById(R.id.title);
		body = (TextView) findViewById(R.id.body);
		link = (TextView) findViewById(R.id.link);
		
		link.setMovementMethod(LinkMovementMethod.getInstance());
		
		notification = (Notification) getIntent().getSerializableExtra("notification");
		title.setText(notification.getTitle());
		new InitDataTask().execute();
	}

	class InitDataTask extends AsyncTask<String, String, String[]> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String[] doInBackground(String... params) {
			String notificationResponse = HttpClientFactory.get(notification.getUrl());
			
			//Get body
			Document notificationDoc = Jsoup.parse(notificationResponse);
			Elements notificationBody = notificationDoc.select("#NR");
			Elements notificationUrls = notificationDoc.select(".download a");
			String url = "";
			for (Element notificationUrl : notificationUrls) {
//				if (notificationUrl.toString().indexOf("__doPostBack") > 0) {
//					notificationUrl.attr("href", getSubmitUrl(notificationUrl.toString()));
//				}
				url += notificationUrl + "<br /><br />";
			}
			
			String[] contents = {notificationBody.toString(),url};
			return contents;
		}
		
		@Override
		protected void onPostExecute(String[] result) {
			body.setText(Html.fromHtml(result[0]));
			link.setText(Html.fromHtml(result[1]));
			super.onPostExecute(result);
		}
	}
	
	//解析下载的url
//	private String getSubmitUrl(String url) {
//		int startOffset = url.indexOf("'") + 1;
//		int secondOffset = url.indexOf("'", startOffset);
//		int thirdOffset = url.indexOf("'", secondOffset + 1);
//		int forthOffset = url.indexOf("'", thirdOffset);
//		String EVENTTARGET = url.substring(startOffset, secondOffset).replace('$', ':');
//		String EVENTARGUMENT = url.substring(thirdOffset, forthOffset);
//		String formUrl = Constant.BASE_DOMAIN + notification.getUrl() +
//				"&__EVENTTARGET=" + EVENTTARGET +
//				"&__EVENTARGUMENT=" + EVENTARGUMENT +
//				"&__VIEWSTATE=dDwtMTAyMzA4NDc3ODt0PDtsPGk8MT47PjtsPHQ8O2w8aTwxPjtpPDU+O2k8Nz47aTwxMz47aTwxNT47PjtsPHQ8cDxwPGw8VGV4dDs+O2w85YWz5LqO5YGa5aW95oiR5qChMjAxMue6p+aZrumAmuWtpueUn+i9rOS4k+S4muW3peS9nOeahOmAmuefpTs+Pjs+Ozs+O3Q8O2w8aTwxPjs+O2w8dDxwPGw8aW5uZXJodG1sOz47bDxcPHBcPuWQhOWtpumZou+8mlw8YnIgL1w+DQrkuLrlhYXliIbmu6HotrPlrabnlJ/kuKrmgKflj5HlsZXnmoTpnIDopoHvvIzlsIrph43lrabnlJ/lrabkuaDlhbTotqPvvIzmj5Dpq5jlrabnlJ/lrabkuaDnmoTkuLvliqjmgKflkoznp6/mnoHmgKfvvIzkv4Pov5vkurrmiY3ln7nlhbvlt6XkvZzmm7Tlpb3lnLDpgILlupTnpL7kvJrlj5HlsZXpnIDopoHvvIzmoLnmja7jgIrnpo/lu7rnnIHmlZnogrLljoXlhbPkuo7ov5vkuIDmraXop4TojIPmma7pgJrpq5jnrYnlrabmoKHlrabnlJ/ovazlrabovazkuJPkuJrnmoTpgJrnn6XjgIvvvIjpl73mlZnlrabjgJQyMDEx44CVMzXlj7fvvInlkozjgIrljqbpl6jnkIblt6XlrabpmaLovazkuJPkuJrnrqHnkIblip7ms5XjgIvvvIjljqbnkIblt6XmlZlbMjAxMV0yN+WPt++8ieetieaWh+S7tueyvuelnu+8jOWumuS6jjIwMTLlubQ05pyIMeaXpS0tLTbmnIgyOOaXpei/m+ihjDIwMTLnuqflrabnlJ/ovazkuJPkuJrlt6XkvZzvvIznjrDlsIbmnInlhbPkuovpobnpgJrnn6XlpoLkuIvvvJpcPGJyIC9cPg0K5LiA44CB6L2s5LiT5Lia5a+56LGhXDxiciAvXD4NCjIwMTLnuqfmma7pgJrlhajml6XliLblnKjmoKHmnKzkuJPnp5HlrabnlJ/jgIJcPGJyIC9cPg0K5LqM44CB55Sz6K+35p2h5Lu2XDxiciAvXD4NCuagueaNruOAiuWOpumXqOeQhuW3peWtpumZoui9rOS4k+S4mueuoeeQhuWKnuazleOAi++8iOWOpueQhuW3peaVmVsyMDExXTI35Y+377yJ77yI6KeB6ZmE5Lu2Me+8ieaWh+S7tuesrOS4ieadoeinhOWumuaJp+ihjOOAglw8YnIgL1w+DQrkuInjgIHovazkuJPkuJrlt6XkvZzopoHmsYJcPGJyIC9cPg0KMeOAgei9rOS4k+S4muW3peS9nOimgeaMieeFp+WuouinguOAgeWFrOato+OAgeWFrOW5s+eahOWOn+WImei/m+ihjO+8jOaLqeS8mOW9leWPluOAglw8YnIgL1w+DQoy44CB5ZCE5a2m6Zmi6KaB5oiQ56uL6L2s5LiT5Lia5bel5L2c6aKG5a+85bCP57uE77yM5ZCI55CG5o6n5Yi26L2s5YWl6L2s5Ye65Lq65pWw5q+U5L6L77yM56ev5p6B5YGa5aW95a2m55Sf55qE5byV5a+85bel5L2c77yM6YG/5YWN5a2m55Sf55uy55uu6YCJ5oup77yM5ZCM5pe257uE57uH55Sx55u45YWz5LiT5Lia55qE5LiT5a6257uE77yI5LiN5bCR5LqOM+S6uu+8ie+8jOmAmui/h+eslOivleaIluiAhemdouivleaWueW8j+WvueaLn+i9rOWFpeWtpueUn++8iOWQq+acrOWtpumZouWGhei9rOS4k+S4muWtpueUn++8iei/m+ihjOiAg+aguO+8jOehruWumuaLn+i9rOWFpeWtpueUn+WQjeWNle+8jOaVtOS4qui/h+eoi+W6lOS7peS5pumdouW9ouW8j+S4iuaKpeaVmeWKoeWkhOOAglw8YnIgL1w+DQozLiDmr4/lkI3lrabnlJ/lnKjmoKHmnJ/pl7Tlj6rog73ovazkuJPkuJrkuIDmrKHvvIzovazkuJPkuJrnlLPor7fkuIDnu4/lrabmoKHmibnlh4bvvIzkuI3lvpflho3mrKHnlLPor7fovazlm57ljp/kuJPkuJrmiJbovazlhaXlhbblroPkuJPkuJrjgIJcPGJyIC9cPg0KNOOAgeaJgOaciei9rOS4k+S4muWtpueUn+Wdh+mhu+aMieeFp+i9rOWFpeS4k+S4muWfueWFu+aWueahiOeahOimgeaxgu+8jOS/ruWujOinhOWumuivvueoi+WSjOWtpuWIhu+8jOaWueS6iOavleS4mu+8jOi+vuWIsOi9rOWFpeS4k+S4muWtpuS9jeaOiOS6iOadoeS7tu+8jOaWueWPr+aOiOS6iOi9rOWFpeS4k+S4muaJgOWcqOWtpuenkeWtpuWjq+WtpuS9jeOAglw8YnIgL1w+DQo144CB6L2s5LiT5Lia5YmN77yM5a2m55Sf5bey5L+u6K++56iL5Y+K5a2m5YiG56ym5ZCI6L2s5YWl5LiT5Lia5Z+55YW75pa55qGI6KaB5rGC55qE77yM57uP6L2s5YWl5a2m6Zmi5ZCM5oSP77yM5a2m5qCh5LqI5Lul6L2s5o2i5oiW56Gu6K6k77yb5LiN56ym5ZCI6L2s5YWl5LiT5Lia5Z+55YW75pa55qGI6KaB5rGC55qE77yM5Y+q5L2c5Li66YCJ5L+u6K++6K6w5b2V77yM5LiN5b6X6L2s5o2i5oiW56Gu6K6k5Li65Z+55YW75pa55qGI6KaB5rGC55qE5a2m5YiG44CCXDxiciAvXD4NCjbjgIHlnKjojrflrabmoKHmibnlh4bliY3vvIznlLPor7fovazkuJPkuJrnmoTlrabnlJ/pobvlnKjljp/kuJPkuJrlnZrmjIHlrabkuaDvvIzlkKbliJnku6Xml7for77lpITnkIbjgIJcPGJyIC9cPg0K5Zub77yO6L2s5LiT5Lia5bel5L2c5pe26Ze05a6J5o6S6KGoXDxiciAvXD4NCuaXtumXtCDlt6XkvZzlhoXlrrlcPGJyIC9cPg0KNOaciDHml6XliY0g5pWZ5Yqh5aSE5LiL5Y+R6YCa55+l5biD572u5pys5a2m5pyf6L2s5LiT5Lia5bel5L2cXDxiciAvXD4NCjTmnIgxMuaXpeWJjSDlkITlrabpmaLmiJDnq4vpooblr7zlsI/nu4TlkozkuJPlrrbnu4TvvIzlubbmoLnmja7mnKzlrabpmaLmlZnlrabotYTmupDmg4XlhrXvvIzmj5Dlh7rmi5/mjqXmlLbovazkuJPkuJrlrabnlJ/nmoTkuJPkuJrjgIHlrabnlJ/mlbDvvIzku6Xlj4rmjqXmlLbmnaHku7bjgIHogIPmoLjlhoXlrrnvvIjljp/liJnkuIropoHmsYLkuKTpl6jor77nqIvvvIznlLHlkITlrabpmaLoh6rlrprvvIzlu7rorq7ogIPmoLjmnKzkuJPkuJrnm7jlhbPnmoTln7rnoYDnn6Xor4bvvIzph43ngrnogIPmoLjlrabnlJ/lrabkuaDog73lipvjgIHlrabkuaDliqjmnLrku6Xlj4rlrabnlJ/kuLrkvZXpgInmi6nmnKzkuJPkuJrvvIzlr7nmnKzkuJPkuJrnmoTkuobop6PmmK/lkKbliLDkvY3vvIzmmK/lkKbmnInog73lipvlrabkuaDlpb3mnKzkuJPkuJrnmoTor77nqIvnrYnvvInlkozogIPmoLjmlrnlvI/vvIjnrJTor5XlkozpnaLor5Xnm7jnu5PlkIjvvInnrYnovazkuJPkuJrlt6XkvZzmlrnmoYjvvIzlubbloavlhpnjgIrljqbpl6jnkIblt6XlrabpmaLlkITlrabpmaLlkITkuJPkuJrmi5/mjqXmlLbovazkuJPkuJrlrab";
//		return formUrl;
//	}
}
