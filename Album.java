package unl.cse.albums;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Album {

	private Integer albumId;
	private String title;
	private Integer year;
	private Band band;
	private Integer albumNumber;
	private List<String> songTitles = new ArrayList<String>();
	
	public Album(Integer albumId, String title, Integer year, Band band,
			Integer albumNumber) {
		super();
		this.albumId = albumId;
		this.title = title;
		this.year = year;
		this.band = band;
		this.albumNumber = albumNumber;
	}

	public Album(String title, Integer year, String bandName) {
		this(null, title, year, new Band(bandName), null);
	}

	public Integer getAlbumId() {
		return albumId;
	}

	public String getTitle() {
		return title;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getAlbumNumber() {
		return albumNumber;
	}

	public Band getBand() {
		return band;
	}

	public List<String> getSongTitles() {
		return songTitles;
	}

	public void addSong(String songTitle) {
		this.songTitles.add(songTitle);
	}
	
	/**
	 * This method returns a {@link #Album} instance loaded from the 
	 * database corresponding to the given <code>albumId</code>.  
	 * Throws an {@link IllegalStateException} upon an invalid <code>albumId</code>.
	 * All fields are loaded with this method.
	 * @param albumId
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static Album getDetailedAlbum(int albumId) throws ClassNotFoundException, SQLException {
		//Class.forName("com.sql.jdbc.Driver");
		List<Album> albumList = new ArrayList<Album>();
		Connection conn = DriverManager.getConnection(DatabaseInfo.url,DatabaseInfo.username,DatabaseInfo.password);
		Statement stmt = conn.createStatement();
		
		Album album = null;
		String query = "SELECT title, year, albumId, bandId, number FROM Album WHERE albumId ="+"" + albumId +"";
		ResultSet rs = stmt.executeQuery(query);
		if(rs.next()) {
			//b = new Band(bandId, rs.getString("name"));
		} else {
			throw new IllegalStateException("no such band with bandId = ");
		}
		String title = rs.getString("title");
		
		Integer year = rs.getInt("year");
		Integer albumNumber = rs.getInt("number");
		Integer bandId = rs.getInt("bandId");
		query = "SELECT name FROM Band WHERE bandId ="+"" + bandId +"";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			//b = new Band(bandId, rs.getString("name"));
		} else {
			throw new IllegalStateException("no such band with bandId = ");
		}
		String name = rs.getString("name");
		album = new Album(title, year, name);
		album.albumNumber = albumNumber;
		Band band = new Band(bandId, name);
		query = "SELECT songId FROM AlbumSong WHERE albumId ="+"" + albumId +"";
		rs = stmt.executeQuery(query);
		
		List<Integer> songIdList = new ArrayList<Integer>();
		//List<String> songTitleList = new ArrayList<String>();
		List<Integer> memberIdList = new ArrayList<Integer>();
		//List<String> memberNameList = new ArrayList<String>();
		
		while(rs.next()) {
			songIdList.add(rs.getInt("songId"));
		}
		for(Integer songId : songIdList) {
			query = "SELECT title FROM Song WHERE songId ="+"" + songId +"";
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				//b = new Band(bandId, rs.getString("name"));
			} else {
				throw new IllegalStateException("no such band with bandId = ");
			}
			album.addSong(rs.getString("title"));
			//songTitleList.add(rs.getString("title"));
		}
		
		query = "SELECT musicianId FROM BandMember WHERE bandId ="+"" + bandId +"";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			memberIdList.add(rs.getInt("musicianId"));
		}
		for(Integer memberId : memberIdList) {
			query = "SELECT firstName, lastName FROM Musician WHERE musicianId ="+"" + memberId +"";
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				//b = new Band(bandId, rs.getString("name"));
			} else {
				throw new IllegalStateException("no such band with bandId = ");
			}
			//band.addMember(rs.getString("firstName" + "lastName"));
			band.addMember(rs.getString("firstName") + rs.getString("lastName"));

			//memberNameList.add(rs.getString("firstName" + "lastName"));
		}
		album.band = band;
		rs.close();
		stmt.close();
		conn.close();
		return album;
	}
	
	/**
	 * Returns a list of all albums in the database.  However, this
	 * is only a summary so only the following items need to be loaded
	 * from the database:
	 * <ul>
	 *   <li>Album ID</li>
	 *   <li>Album Title</li>
	 *   <li>Album Year</li>
	 *   <li>Band ID</li>
	 *   <li>Band Name</li>
	 * </ul>
	 *   
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static List<Album> getAlbumSummaries() throws ClassNotFoundException, SQLException {
		//Class.forName("com.sql.jdbc.Driver");
		List<Album> albumList = new ArrayList<Album>();
		Connection conn = DriverManager.getConnection(DatabaseInfo.url,DatabaseInfo.username,DatabaseInfo.password);
		Statement stmt = conn.createStatement();
		String query = "SELECT a.albumId, a.title, a.year, a.bandId, a.number, b.name FROM Album a JOIN Band b ON a.bandId = b.bandId";
		ResultSet rs = stmt.executeQuery(query);
		
		while(rs.next()) {
			Integer albumId = rs.getInt("albumId");
			String title = rs.getString("title");
			Integer year = rs.getInt("year");
			Integer bandId = rs.getInt("bandId");
			String name = rs.getString("name");
			Integer albumNumber = rs.getInt("number");
			Band band = new Band(bandId, name);
			Album album = new Album(albumId, title, year, band, albumNumber);
			albumList.add(album);
		}
		rs.close();
		stmt.close();
		conn.close();
		return albumList;
		
	}

	@Override
	public String toString() {
		return "Album [albumId=" + albumId + ", title=" + title + ", year="
				+ year + ", band=" + band + ", albumNumber=" + albumNumber
				+ ", songTitles=" + songTitles + "]";
	}
	
	
	
	
}
