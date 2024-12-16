package dev.nautchkafe.postgres;

final record PgConnectionCredentials(
	String url, 
	String username, 
	String password
) { 
}
