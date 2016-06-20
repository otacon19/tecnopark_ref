package sinbad2.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseHelper {

	public DatabaseHelper() {
		super();
	}

	public void createTableStatement(Connection connection, String tableName, String[] columns, String[] properties,
			String[] keys) throws SQLException {
		Statement statement = connection.createStatement();
		createTable(statement, tableName, columns, properties, keys);
		statement.close();
	}

	public void createTable(Statement statement, String tableName, String[] columns, String[] properties, String[] keys)
			throws SQLException {
		if (validCreateStatement(tableName, columns, properties, keys)) {
			String sql = generateCreateTableStatement(tableName, columns, properties, keys);
			executeUpdate(statement, sql);
		} else {
			throw new SQLException("Invalid create table statement"); // $NON-NLS-1$
		}
	}

	private boolean validCreateStatement(String tableName, String[] columns, String[] properties, String[] keys) {
		if (notEmptyString(tableName)) {
			if (arrayOfNotEmptyStrings(columns)) {
				if (arrayOfNotEmptyStrings(properties)) {
					if (columns.length == properties.length) {
						if (keys != null) {
							if (columns.length >= keys.length) {
								Set<String> columnsMap = new HashSet<String>();
								for (String column : columns) {
									columnsMap.add(column);
								}
								for (String key : keys) {
									if (!columnsMap.contains(key)) {
										return false;
									}
								}
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	private boolean notEmptyString(String value) {
		if (value != null) {
			if (!value.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private boolean arrayOfNotEmptyStrings(String[] array) {
		if (notEmptyArray(array)) {
			for (String element : array) {
				if (!notEmptyString(element)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private boolean notEmptyArray(Object[] array) {
		if (array != null) {
			if (array.length > 0) {
				return true;
			}
		}
		return false;
	}

	private String generateCreateTableStatement(String tableName, String[] columns, String[] properties,
			String[] keys) {
		StringBuilder result = new StringBuilder();
		result.append("create table if not exists " + tableName); // $NON-NLS-1$
		result.append(generateCreateValues(columns, properties, keys));
		result.append(";"); // $NON-NLS-1$
		return result.toString();
	}

	private String generateCreateValues(String[] columns, String[] properties, String[] keys) {
		StringBuilder result = new StringBuilder();
		if (columns.length > 0) {
			String[] columnsWithProperties = generateColumnsWithProperties(columns, properties);
			result.append(generateListOfValues(columnsWithProperties));
			addToSeparateListOfValues(result, generatePrimaryKey(keys));
			inParentheses(result);
		}
		return result.toString();
	}

	private String[] generateColumnsWithProperties(String[] columns, String[] properties) {
		String[] result = new String[columns.length];
		for (int column = 0; column < columns.length; column++) {
			result[column] = columns[column] + " " + properties[column]; // $NON-NLS-1$
		}
		return result;
	}

	private StringBuilder inParentheses(StringBuilder value) {
		if (value.length() > 0) {
			value.insert(0, "("); // $NON-NLS-1$
			value.append(")"); // $NON-NLS-1$
		}
		return value;
	}

	private StringBuilder generatePrimaryKey(String[] keys) {
		StringBuilder result = new StringBuilder();
		if (keys.length > 0) {
			result.append("primary key "); // $NON-NLS-1$
			result.append(inParentheses(generateListOfValues(keys)));
		}
		return result;
	}

	private StringBuilder generateListOfValues(String[] values) {
		StringBuilder result = new StringBuilder();
		for (String value : values) {
			addToSeparateListOfValues(result, value);
		}
		return result;
	}

	private StringBuilder addToSeparateListOfValues(StringBuilder listOfValues, String value) {
		if (!value.isEmpty()) {
			if (listOfValues.length() > 0) {
				listOfValues.append(", "); // $NON-NLS-1$
			}
			listOfValues.append(value);
		}
		return listOfValues;
	}

	private void addToSeparateListOfValues(StringBuilder listOfValues, StringBuilder value) {
		addToSeparateListOfValues(listOfValues, value.toString());
	}

	public void dropTableStatement(Connection connection, String[] tableNames) throws SQLException {
		Statement statement = connection.createStatement();
		dropTable(statement, tableNames);
		statement.close();
	}

	public void dropTable(Statement statement, String[] tableNames) throws SQLException {
		if (validDropStatement(tableNames)) {
			String sql = generateDropTableStatement(tableNames);
			executeUpdate(statement, sql);
		} else {
			throw new SQLException("Invalid drop table statement"); // $NON-NLS-1$
		}
	}

	private boolean validDropStatement(String[] tableNames) {
		return arrayOfNotEmptyStrings(tableNames);
	}

	private String generateDropTableStatement(String[] tableNames) {
		StringBuilder result = new StringBuilder();
		result.append("drop table if exists "); // $NON-NLS-1$
		result.append(generateListOfValues(tableNames));
		result.append(";"); // $NON-NLS-1$
		return result.toString();
	}

	public void insertIntoStatement(Connection connection, String tableName, String[] columns, String[] values,
			Boolean[] strings) throws SQLException {
		Statement statement = connection.createStatement();
		insertInto(statement, tableName, columns, values, strings);
		statement.close();
	}

	public void insertInto(Statement statement, String tableName, String[] columns, String[] values, Boolean[] strings)
			throws SQLException {
		if (validInsertIntoStatement(tableName, columns, values, strings)) {
			String sql = generateInsertIntoStatement(tableName, columns, values, strings);
			executeUpdate(statement, sql);
		} else {
			throw new SQLException("Invalid insert into statement"); // $NON-NLS-1$
		}
	}

	private boolean validInsertIntoStatement(String tableName, String[] columns, String[] values, Boolean[] strings) {
		if (tableName != null) {
			if (tableName.length() > 0) {
				if (arrayOfNotEmptyStrings(columns) && arrayOfNotEmptyStrings(values)
						&& arrayOfNotNullElements(strings)) {
					if ((columns.length == values.length) && (columns.length == strings.length)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean arrayOfNotNullElements(Object[] array) {
		if (notEmptyArray(array)) {
			for (Object element : array) {
				if (element == null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private String generateInsertIntoStatement(String tableName, String[] columns, String[] values, Boolean[] strings) {
		StringBuilder result = new StringBuilder();
		result.append("insert into " + tableName); // $NON-NLS-1$
		result.append(generateInsertIntoColumns(columns));
		result.append(" values "); // $NON-NLS-1$
		result.append(generateInsertIntoValues(values, strings));
		result.append(";"); // $NON-NLS-1$
		return result.toString();
	}

	private String generateInsertIntoColumns(String[] columns) {
		StringBuilder result = new StringBuilder();
		result.append(inParentheses(generateListOfValues(columns)));
		return result.toString();
	}

	private String generateInsertIntoValues(String[] values, Boolean[] strings) {
		StringBuilder result = new StringBuilder();
		String[] quotedValues = quoteValues(values, strings);
		result.append(inParentheses(generateListOfValues(quotedValues)));
		return result.toString();
	}

	private String[] quoteValues(String[] values, Boolean[] strings) {
		String[] result = new String[values.length];
		for (int pos = 0; pos < values.length; pos++) {
			result[pos] = (strings[pos]) ? quoteValue(values[pos]) : values[pos];
		}
		return result;
	}

	private String quoteValue(String value) {
		return "'" + value + "'"; // $NON-NLS-1$ // $NON-NLS-2$
	}

	public void deleteFromStatement(Connection connection, String tableName, String[] columns, String[] values,
			Boolean[] strings) throws SQLException {
		Statement statement = connection.createStatement();
		deleteFrom(statement, tableName, columns, values, strings);
		statement.close();
	}

	public void deleteFrom(Statement statement, String tableName, String[] columns, String[] values, Boolean[] strings)
			throws SQLException {
		if (validDeleteFromStatement(tableName, columns, values, strings)) {
			String sql = generateDeleteFromStatement(tableName, columns, values, strings);
			executeUpdate(statement, sql);
		} else {
			throw new SQLException("Invalid delete from statement"); // $NON-NLS-1$
		}
	}

	private boolean validDeleteFromStatement(String tableName, String[] columns, String[] values, Boolean[] strings) {
		return validInsertIntoStatement(tableName, columns, values, strings);
	}

	private String generateDeleteFromStatement(String tableName, String[] columns, String[] values, Boolean[] strings) {
		StringBuilder result = new StringBuilder();
		result.append("delete from " + tableName + " where "); //$NON-NLS-1$ //$NON-NLS-2$
		result.append(generateDeleteFromValues(columns, values, strings));
		result.append(";"); // $NON-NLS-1$
		return result.toString();
	}

	private String generateDeleteFromValues(String[] columns, String[] values, Boolean[] strings) {
		StringBuilder result = new StringBuilder();
		String[] conditions = generateValuesConditions(columns, values, strings);
		for (String condition : conditions) {
			addCondition(result, condition);
		}
		return result.toString();
	}

	private String[] generateValuesConditions(String[] columns, String[] values, Boolean[] strings) {
		String[] result = new String[columns.length];
		String condition;
		for (int column = 0; column < columns.length; column++) {
			condition = (strings[column]) ? quoteValue(values[column]) : values[column];
			result[column] = columns[column] + "=" + condition; // $NON-NLS-1$
		}
		return result;
	}

	private StringBuilder addCondition(StringBuilder listOfValues, String value) {
		if (!value.isEmpty()) {
			if (listOfValues.length() > 0) {
				listOfValues.append(" and "); // $NON-NLS-1$
			}
			listOfValues.append(value);
		}
		return listOfValues;
	}

	public List<Map<String, Object>> selectStatement(Connection connection, String tableName, String[] columns,
			String[] parameters, String[] conditions, String[] values, Boolean[] strings) throws SQLException {
		List<Map<String, Object>> result;
		Statement statement = connection.createStatement();
		result = select(statement, tableName, columns, parameters, conditions, values, strings);
		statement.close();
		return result;
	}

	public List<Map<String, Object>> select(Statement statement, String tableName, String[] columns,
			String[] parameters, String[] conditions, String[] values, Boolean[] strings) throws SQLException {
		if (validSelectStatement(tableName, columns, parameters, conditions, values, strings)) {
			String sql = generateSelectStatement(tableName, columns, parameters, conditions, values, strings);
			ResultSet resultSet = executeQuery(statement, sql);
			return extractResultSetValues(resultSet);
		} else {
			throw new SQLException("Invalid select statement"); // $NON-NLS-1$
		}
	}

	private boolean validSelectStatement(String tableName, String[] columns, String[] parameters, String[] conditions,
			String[] values, Boolean[] strings) {
		if (tableName != null) {
			if (tableName.length() > 0) {
				if (arrayOfNotEmptyStrings(columns) && arrayOfNotEmptyStrings(parameters)
						&& arrayOfNotEmptyStrings(conditions) && arrayOfNotEmptyStrings(values)
						&& arrayOfNotNullElements(strings)) {
					if ((parameters.length == conditions.length) && (conditions.length == values.length)
							&& (values.length == strings.length)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private String generateSelectStatement(String tableName, String[] columns, String[] parameters, String[] conditions,
			String[] values, Boolean[] strings) {
		StringBuilder result = new StringBuilder();
		result.append("select "); // $NON-NLS-1$
		result.append(generateListOfValues(columns));
		result.append(" from "); // $NON-NLS-1$
		result.append(tableName);
		result.append(" where ");
		result.append(generateSelectConditions(parameters, conditions, values, strings));
		result.append(";"); // $NON-NLS-1$
		return result.toString();
	}

	private String generateSelectConditions(String[] parameters, String[] conditions, String[] values,
			Boolean[] strings) {
		StringBuilder result = new StringBuilder();
		String[] parametersWithConditions = generateConditions(parameters, conditions, values, strings);
		for (String parameterWithCondition : parametersWithConditions) {
			addCondition(result, parameterWithCondition);
		}
		return result.toString();
	}

	private String[] generateConditions(String[] parameters, String[] conditions, String[] values, Boolean[] strings) {
		String[] result = new String[parameters.length];
		StringBuilder condition;
		for (int parameter = 0; parameter < parameters.length; parameter++) {
			condition = new StringBuilder();
			condition.append(parameters[parameter]);
			condition.append(" "); // $NON-NLS-1$
			condition.append(conditions[parameter]);
			condition.append(" "); // $NON-NLS-1$
			condition.append((strings[parameter]) ? quoteValue(values[parameter]) : values[parameter]);
			result[parameter] = condition.toString();
		}
		return result;
	}

	private List<Map<String, Object>> extractResultSetValues(ResultSet resultSet) throws SQLException {
		List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();
		Map<String, Object> row;
		while (resultSet.next()) {
			row = new HashMap<String, Object>();
			for (int column = 1; column <= resultSet.getMetaData().getColumnCount(); column++) {
				row.put(resultSet.getMetaData().getColumnLabel(column), resultSet.getObject(column));
			}
			result.add(row);
		}
		return result;
	}

	public List<Map<String, Object>> selectAllStatement(Connection connection, String tableName) throws SQLException {
		List<Map<String, Object>> result;
		Statement statement = connection.createStatement();
		result = selectAll(statement, tableName);
		statement.close();
		return result;
	}

	public List<Map<String, Object>> selectAll(Statement statement, String tableName) throws SQLException {
		if (validSelectAllStatement(tableName)) {
			String sql = generateSelectAllStatement(tableName);
			ResultSet resultSet = executeQuery(statement, sql);
			return extractResultSetValues(resultSet);
		} else {
			throw new SQLException("Invalid select statement"); // $NON-NLS-1$
		}
	}
	
	private boolean validSelectAllStatement(String tableName) {
		return notEmptyString(tableName);
	}
	
	private String generateSelectAllStatement(String tableName) {
		return "select * from " + tableName + ";"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void updateStatement(Connection connection, String tableName, String[] columnsToModify, String[] newValues,
			Boolean[] newValuesStrings, String[] parameters, String[] conditions, String[] values, Boolean[] strings)
					throws SQLException {
		Statement statement = connection.createStatement();
		update(statement, tableName, columnsToModify, newValues, newValuesStrings, parameters, conditions, values,
				strings);
		statement.close();
	}

	public void update(Statement statement, String tableName, String[] columnsToModify, String[] newValues,
			Boolean[] newValuesStrings, String[] parameters, String[] conditions, String[] values, Boolean[] strings)
					throws SQLException {
		if (validUpdateStatement(tableName, columnsToModify, newValues, newValuesStrings, parameters, conditions,
				values, strings)) {
			String sql = generateUpdateStatement(tableName, columnsToModify, newValues, newValuesStrings, parameters,
					conditions, values, strings);
			executeUpdate(statement, sql);
		} else {
			throw new SQLException("Invalid update statement"); // $NON-NLS-1$
		}
	}

	private boolean validUpdateStatement(String tableName, String[] columnsToModify, String[] newValues,
			Boolean[] newValuesStrings, String[] parameters, String[] conditions, String[] values, Boolean[] strings)
					throws SQLException {
		if (tableName != null) {
			if (tableName.length() > 0) {
				if (arrayOfNotEmptyStrings(columnsToModify) && arrayOfNotEmptyStrings(newValues)
						&& arrayOfNotEmptyStrings(parameters) && arrayOfNotEmptyStrings(conditions)
						&& arrayOfNotEmptyStrings(values) && arrayOfNotNullElements(strings)) {
					if ((columnsToModify.length == newValues.length) && (newValues.length == newValuesStrings.length)) {
						if ((parameters.length == conditions.length) && (conditions.length == values.length)
								&& (values.length == strings.length)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private String generateUpdateStatement(String tableName, String[] columnsToModify, String[] newValues,
			Boolean[] newValuesStrings, String[] parameters, String[] conditions, String[] values, Boolean[] strings) {
		StringBuilder result = new StringBuilder();
		result.append("update "); // $NON-NLS-1$
		result.append(tableName);
		result.append(" set "); // $NON-NLS-1$
		result.append(generateUpdateValues(columnsToModify, newValues, newValuesStrings));
		result.append(" where ");
		result.append(generateSelectConditions(parameters, conditions, values, strings));
		result.append(";"); // $NON-NLS-1$
		return result.toString();
	}

	private String generateUpdateValues(String[] columnsToModify, String[] newValues, Boolean[] newValuesStrings) {
		StringBuilder result = new StringBuilder();
		StringBuilder value;
		for (int column = 0; column < columnsToModify.length; column++) {
			value = new StringBuilder();
			value.append(columnsToModify[column]);
			value.append(" = "); // $NON-NLS-1$
			value.append((newValuesStrings[column]) ? quoteValue(newValues[column]) : newValues[column]);
			addToSeparateListOfValues(result, value);
		}
		return result.toString();
	}

	public void executeUpdateStatement(Connection connection, String sql) throws SQLException {
		Statement statement = connection.createStatement();
		executeUpdate(statement, sql);
		statement.close();
	}

	public void executeUpdate(Statement statement, String sql) throws SQLException {
		statement.executeUpdate(sql);
	}

	public ResultSet executeQueryStatement(Connection connection, String sql) throws SQLException {
		ResultSet result;
		Statement statement = connection.createStatement();
		result = executeQuery(statement, sql);
		statement.close();
		return result;
	}

	public ResultSet executeQuery(Statement statement, String sql) throws SQLException {
		return statement.executeQuery(sql);
	}
}
