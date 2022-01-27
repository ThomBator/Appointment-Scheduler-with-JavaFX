package DAO;

import com.mysql.cj.xdevapi.PreparableStatement;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CountryQuery {
    private List<Country> countries = new ArrayList<>();
    private PreparedStatement preparedStatement = DBQuery.getPreparedStatement();
    private final String SELECTSTATEMENT = "SELECT * FROM countries";








}
