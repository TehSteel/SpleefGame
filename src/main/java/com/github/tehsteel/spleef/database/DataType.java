package com.github.tehsteel.spleef.database;

public enum DataType {
	SQLITE,
	MYSQL,
	MONGODB;

	public static DataType fromString(final String dataType) {
		if (dataType.equalsIgnoreCase("mysql")) {
			return MYSQL;
		} else if (dataType.equalsIgnoreCase("mongodb")) {
			return MONGODB;
		}


		return SQLITE;
	}
}
