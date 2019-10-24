package unl.cse.albums;

import java.sql.SQLException;
import java.util.List;

public class Test {

	public static void main(String args[]) throws ClassNotFoundException, SQLException {

		List<Album> disc = Album.getAlbumSummaries();

		for(Album a : disc) {
			System.out.println(a.getTitle() + " (id = "+a.getAlbumId()+") by " + a.getBand().getName() + " (id = "+a.getBand().getBandId()+"), " + a.getYear());
		}
		Album a = null;
		for(String title : a.getDetailedAlbum(89108).getSongTitles())
			System.out.println(title);
		}
}