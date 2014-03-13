NewAndroidTwitter
=================

NewAndroidTwitter is Android wrapper library to access [Twitter REST API version 1.1][4]. You can use this library to implement
"Signing in with Twitter" feature in your application or can be used to access all Twitter REST API endpoints such as post status, get timeline etc. This is an enhanchment from my previous [library][3] which depends on Twitter4j. In this new version, you don't have to use Twitter4j anymore. Feel free to use it all you want in your Android apps.

If you are using NewAndroidTwitter in your app and would like to be listed here, please let me know via [Twitter][1]!

Also, you can follow me on Twitter : [@lorensiuswlt][1] or visit my blog [www.londatiga.net][2]

![Example Image](http://londatiga.net/images/android_twitter_api2.png) 

Setup
-----
* In Eclipse, just import the library as an Android library project. Project > Clean to generate the binaries 
you need, like R.java, etc.
* Then, just add NewAndroidTwitter as a dependency to your existing project.


Simple Example
-----
This sample shows how to signing in with twitter, getting credentials and call Twitter API endpoints v1.1

```java
public class MainActivity extends Activity {
    private Twitter mTwitter;
    
    public static final String CONSUMER_KEY = "your consumer key";
    public static final String CONSUMER_SECRET = "your secret key";
    public static final String CALLBACK_URL = "your callback url";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        mTwitter = new Twitter(this, CONSUMER_KEY, CONSUMER_SECRET, CALLBACK_URL);
        
        if (!mTwitter.sessionActive()) {
            ((Button) findViewById(R.id.btn_connect)).setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View arg0) {
                    signinTwitter();
                }
            });

            ((Button) findViewById(R.id.btn_signout)).setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View arg0) {
                    mTwitter.clearSession();
                }
            });
        }       
    }

    private void signinTwitter() {
        mTwitter.signin(new Twitter.SigninListener() {              
            @Override
            public void onSuccess(OauthAccessToken accessToken, String userId, String screenName) {
                //success

                getCredentials();
            }
            
            @Override
            public void onError(String error) {
                //error
            }
        });
    }

    private void getCredentials() {
        final ProgressDialog progressDlg = new ProgressDialog(this);
        
        progressDlg.setMessage("Getting credentials...");
        progressDlg.setCancelable(false);
        
        progressDlg.show();
        
        TwitterRequest request = new TwitterRequest(mTwitter.getConsumer(), mTwitter.getAccessToken());
        
        request.verifyCredentials(new TwitterRequest.VerifyCredentialListener() {
            
            @Override
            public void onSuccess(TwitterUser user) {
                progressDlg.dismiss();
                
                showToast("Hello " + user.name);
            }
            
            @Override
            public void onError(String error) {
                progressDlg.dismiss();
                
                showToast(error);
            }
        });
    }

    private void updateStatus(String status) {
        final ProgressDialog progressDlg = new ProgressDialog(this);
        
        progressDlg.setMessage("Sending...");
        progressDlg.setCancelable(false);
        
        progressDlg.show();
        
        TwitterRequest request      = new TwitterRequest(mTwitter.getConsumer(), mTwitter.getAccessToken());
        
        String updateStatusUrl      = "https://api.twitter.com/1.1/statuses/update.json";
        
        List<NameValuePair> params  = new ArrayList<NameValuePair>(1);
        
        params.add(new BasicNameValuePair("status", status));
        
        request.createRequest("POST", updateStatusUrl, params, new TwitterRequest.RequestListener() {
            
            @Override
            public void onSuccess(String response) {
                progressDlg.dismiss();
                
                showToast(response);
            }
            
            @Override
            public void onError(String error) {
                showToast(error);
                
                progressDlg.dismiss();
            }
        });
    }
}
```

1. The main class is `Twitter`. Just instantiate the class and provide the consumer key, consumer secret, callback url into its constructor.
2. If signing-in process success, you will get access token, Twitter userid and screen name. Using this access token, you can access all Twitter API endpoints.
3. To sign out, just call the `clearSession()`.
4. To get user credentials, use `TwitterRequest.verifyCredentials`
5. To access Twitter API endpoints, use `createRequest(String method, String url,  List<NameValuePair> params, RequestListener listener)` from `TwitterRequest` class. Uppon success, this method will return raw response in json format and you have to manually parse it. Note that this is an asynchronous call so you can use callback or listener to handle the result.
      

Developed By
------------
* Lorensius W. L. T - <lorenz@londatiga.net>

Website
-------
* [www.londatiga.net][2]

License
-------

    Copyright 2014 Lorensius W. L. T

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
[1]: http://twitter.com/lorensiuswlt
[2]: http://www.londatiga.net
[3]: https://github.com/lorensiuswlt/AndroidTwitter
[4]: https://dev.twitter.com/docs/api/1.1