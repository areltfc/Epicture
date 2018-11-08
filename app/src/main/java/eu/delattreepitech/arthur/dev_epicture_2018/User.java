package eu.delattreepitech.arthur.dev_epicture_2018;

import android.net.UrlQuerySanitizer;

public class User {
    private String accessToken;
    private String refreshToken;
    private String accountId;
    private String accountUsername;
    private String tokenType;
    private String expiresIn;

    public User(String toParse) {
        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(toParse.replace('#', '?'));
        this.refreshToken = sanitizer.getValue("refresh_token");
        this.accessToken = sanitizer.getValue("access_token");
        this.accountId = sanitizer.getValue("account_id");
        this.accountUsername = sanitizer.getValue("account_username");
        this.tokenType = sanitizer.getValue("token_type");
        this.expiresIn = sanitizer.getValue("expires_in");
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getAccountId() { return accountId; }
    public String getAccountUsername() { return accountUsername; }
    public String getTokenType() { return tokenType; }
    public String getExpiresIn() { return expiresIn; }
}
