package com.github.tehsteel.spleef.database;

public enum DataType {
	SQLITE,
	MYSQL,
	MONGODB;

	public static DataType fromString(final String dataType) throws DataException {
		if (dataType.equalsIgnoreCase("mysql")) {
			return MYSQL;
		}

		if (dataType.equalsIgnoreCase("mongodb")) {
			return MONGODB;
		}

		if (dataType.equalsIgnoreCase("sqlite")) {
			return SQLITE;
		}


		throw new DataException("No allowed database seleceted");
	}
}
