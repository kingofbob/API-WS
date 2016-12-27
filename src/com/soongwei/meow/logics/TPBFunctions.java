package com.soongwei.meow.logics;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import com.soongwei.meow.constants.ContentTypes;
import com.soongwei.meow.objects.json.Request.SearchRequest;
import com.soongwei.meow.objects.json.Respond.TPBResults;
import com.soongwei.meow.utils.HttpHelper;

public class TPBFunctions {

	private static final String DOMAIN = "https://thepiratebay.org";

	public static List<TPBResults> search(SearchRequest searchRequest) {

		try {

			String url = DOMAIN + parseURL(searchRequest);

			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet httpget = new HttpGet(url);

			HttpResponse response = httpClient.execute(httpget);

			// Read HTML response
			InputStream instream = response.getEntity().getContent();
			String html = HttpHelper.convertStreamToString(instream);
			// logger.debug("html respond: " + html);
			instream.close();
			return parseHtml(html);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static String parseURL(SearchRequest searchRequest) throws UnsupportedEncodingException {

		String result = "";
		switch (searchRequest.getType()) {
		case ContentTypes.SEARCH:
			result = "/search/" + searchRequest.getSearch() + "/0/99/0";
			break;

		case ContentTypes.MOVIES:
			result = "/top/207";
			break;

		case ContentTypes.TVSERIES:
			result = "/top/208";
			break;

		case ContentTypes.MUSIC:
			result = "/top/101";
			break;

		case ContentTypes.GAMES:
			result = "/top/400";
			break;

		case ContentTypes.PORNS:
			result = "/top/500";
			break;

		case ContentTypes.APPLICATIONS:
			result = "/top/300";
			break;

		case ContentTypes.EBOOKS:
			result = "/top/601";
			break;

		case ContentTypes.COMICS:
			result = "/top/602";
			break;

		default:
			break;
		}

		result = HttpHelper.encodeURIComponent(result);

		return result;

	}

	protected static List<TPBResults> parseHtml(String html) {

		try {
			// Texts to find subsequently
			final String RESULTS = "<table id=\"searchResult\">";
			final String TORRENT = "<td class=\"vertTh\">";

			// Parse the search results from HTML by looking for the identifying
			// texts
			List<TPBResults> results = new ArrayList<>();
			int resultsStart = html.indexOf(RESULTS) + RESULTS.length();

			int torStart = html.indexOf(TORRENT, resultsStart);
			while (torStart >= 0) {
				int nextTorrentIndex = html.indexOf(TORRENT, torStart + TORRENT.length());
				if (nextTorrentIndex >= 0) {
					try {
						results.add(parseHtmlItem(html.substring(torStart + TORRENT.length(), nextTorrentIndex)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						results.add(parseHtmlItem(html.substring(torStart + TORRENT.length())));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				torStart = nextTorrentIndex;
			}
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static TPBResults parseHtmlItem(String htmlItem) throws Exception {

		// Texts to find subsequently
		final String TYPE = " title=\"More from this category\">";
		final String TYPE_END = "</a><br />";
		final String TYPE2 = " title=\"More from this category\">";
		final String TYPE_END2 = "</a>)";

		final String DETAILS = "<div class=\"detName\">			<a href=\"";
		final String DETAILS_END = "\" class=\"detLink\"";
		final String NAME = "\">";
		final String NAME_END = "</a>";
		final String MAGNET_LINK = "<a href=\"";
		final String MAGNET_LINK_END = "\" title=\"Download this torrent using magnet";
		final String DATE = "detDesc\">Uploaded ";
		final String DATE_END = ", Size ";
		final String SIZE = ", Size ";
		final String SIZE_END = ", ULed by";
		final String SEEDERS = "<td align=\"right\">";
		final String SEEDERS_END = "</td>";
		final String LEECHERS = "<td align=\"right\">";
		final String LEECHERS_END = "</td>";

		String prefixDetails = DOMAIN;
		String prefixYear = (new Date().getYear() + 1900) + " "; // Date.getYear()
																	// gives
																	// the
																	// current
																	// year
																	// -
																	// 1900
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy MM-dd HH:mm", Locale.US);
		SimpleDateFormat df2 = new SimpleDateFormat("MM-dd yyyy", Locale.US);

		int typeStart = htmlItem.indexOf(TYPE) + TYPE.length();
		String typeText = htmlItem.substring(typeStart, htmlItem.indexOf(TYPE_END, typeStart));
		int typeStart2 = htmlItem.indexOf(TYPE2, typeStart) + TYPE2.length();
		String typeText2 = htmlItem.substring(typeStart2, htmlItem.indexOf(TYPE_END2, typeStart2));

		int detailsStart = htmlItem.indexOf(DETAILS, typeStart) + DETAILS.length();
		String details = htmlItem.substring(detailsStart, htmlItem.indexOf(DETAILS_END, detailsStart));
		details = prefixDetails + details;
		int nameStart = htmlItem.indexOf(NAME, detailsStart) + NAME.length();
		String name = htmlItem.substring(nameStart, htmlItem.indexOf(NAME_END, nameStart));

		// Magnet link is first
		int magnetLinkStart = htmlItem.indexOf(MAGNET_LINK, nameStart) + MAGNET_LINK.length();
		String magnetLink = htmlItem.substring(magnetLinkStart, htmlItem.indexOf(MAGNET_LINK_END, magnetLinkStart));

		int dateStart = htmlItem.indexOf(DATE, magnetLinkStart) + DATE.length();
		String dateText = htmlItem.substring(dateStart, htmlItem.indexOf(DATE_END, dateStart));
		dateText = dateText.replace("&nbsp;", " ");
		Date date = null;
		try {
			date = df1.parse(prefixYear + dateText);
		} catch (ParseException e) {
			try {
				date = df2.parse(dateText);
			} catch (ParseException e1) {
				// Not parsable at all; just leave it at null
			}
		}
		int sizeStart = htmlItem.indexOf(SIZE, dateStart) + SIZE.length();
		String size = htmlItem.substring(sizeStart, htmlItem.indexOf(SIZE_END, sizeStart));
		size = size.replace("&nbsp;", " ");
		size = size.replace("i", "");
		int seedersStart = htmlItem.indexOf(SEEDERS, sizeStart) + SEEDERS.length();
		String seedersText = htmlItem.substring(seedersStart, htmlItem.indexOf(SEEDERS_END, seedersStart));
		int seeders = Integer.parseInt(seedersText);
		int leechersStart = htmlItem.indexOf(LEECHERS, seedersStart) + LEECHERS.length();
		String leechersText = htmlItem.substring(leechersStart, htmlItem.indexOf(LEECHERS_END, leechersStart));
		int leechers = Integer.parseInt(leechersText);

		typeText2 = typeText2.replace("HD - ", "");
		typeText2 = typeText2.replace(" DVDR", "");
		typeText2 = typeText2.replace(" shows", "");

		return new TPBResults(name, magnetLink, dateText, seeders + "", size, typeText, typeText2);

	}

}
