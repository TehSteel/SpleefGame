package com.github.tehsteel.spleef.database;

import com.github.tehsteel.spleef.database.impl.SQLDatabase;
import lombok.Getter;

@Getter public final class DatabaseManager {

	private final DataType dataType;
	private IDatabase database;


	public DatabaseManager(final DataType dataType) {
		this.dataType = dataType;
		loadDatabase();
	}

	private void loadDatabase() {
		if (dataType == DataType.MYSQL || dataType == DataType.SQLITE) {
			database = new SQLDatabase();
		}
	}
}
