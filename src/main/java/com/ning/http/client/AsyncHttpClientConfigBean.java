/*
 * Copyright (c) 2010-2012 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package com.ning.http.client;

import static com.ning.http.client.AsyncHttpClientConfigDefaults.*;

import com.ning.http.client.filter.IOExceptionFilter;
import com.ning.http.client.filter.RequestFilter;
import com.ning.http.client.filter.ResponseFilter;
import com.ning.http.util.ProxyUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Simple JavaBean version of  {@link AsyncHttpClientConfig}
 */
public class AsyncHttpClientConfigBean extends AsyncHttpClientConfig {

    public AsyncHttpClientConfigBean() {
        configureExecutors();
        configureDefaults();
        configureFilters();
    }

    void configureFilters() {
        requestFilters = new LinkedList<RequestFilter>();
        responseFilters = new LinkedList<ResponseFilter>();
        ioExceptionFilters = new LinkedList<IOExceptionFilter>();
    }

    void configureDefaults() {
        maxTotalConnections = defaultMaxTotalConnections();
        maxConnectionPerHost = defaultMaxConnectionPerHost();
        connectionTimeOutInMs = defaultConnectionTimeOutInMs();
        webSocketIdleTimeoutInMs = defaultWebSocketIdleTimeoutInMs();
        idleConnectionInPoolTimeoutInMs = defaultIdleConnectionInPoolTimeoutInMs();
        idleConnectionTimeoutInMs = defaultIdleConnectionTimeoutInMs();
        requestTimeoutInMs = defaultRequestTimeoutInMs();
        maxConnectionLifeTimeInMs = defaultMaxConnectionLifeTimeInMs();
        followRedirect = defaultFollowRedirect();
        maxRedirects = defaultMaxRedirects();
        compressionEnabled = defaultCompressionEnabled();
        userAgent = defaultUserAgent();
        allowPoolingConnection = defaultAllowPoolingConnection();
        useRelativeURIsWithSSLProxies = defaultUseRelativeURIsWithSSLProxies();
        requestCompressionLevel = defaultRequestCompressionLevel();
        maxRequestRetry = defaultMaxRequestRetry();
        ioThreadMultiplier = defaultIoThreadMultiplier();
        allowSslConnectionPool = defaultAllowSslConnectionPool();
        disableUrlEncodingForBoundedRequests = defaultDisableUrlEncodingForBoundedRequests();
        removeQueryParamOnRedirect = defaultRemoveQueryParamOnRedirect();
        strict302Handling = defaultStrict302Handling();
        hostnameVerifier = defaultHostnameVerifier();
        acceptAnyCertificate = defaultAcceptAnyCertificate();

        if (defaultUseProxySelector()) {
            proxyServerSelector = ProxyUtils.getJdkDefaultProxyServerSelector();
        } else if (defaultUseProxyProperties()) {
            proxyServerSelector = ProxyUtils.createProxyServerSelector(System.getProperties());
        }
    }

    void configureExecutors() {
        applicationThreadPool = Executors.newCachedThreadPool(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "AsyncHttpClient-Callback");
                t.setDaemon(true);
                return t;
            }
        });
    }

    public AsyncHttpClientConfigBean setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
        return this;
    }

    public AsyncHttpClientConfigBean setMaxConnectionPerHost(int maxConnectionPerHost) {
        this.maxConnectionPerHost = maxConnectionPerHost;
        return this;
    }

    public AsyncHttpClientConfigBean setConnectionTimeOutInMs(int connectionTimeOutInMs) {
        this.connectionTimeOutInMs = connectionTimeOutInMs;
        return this;
    }

    public AsyncHttpClientConfigBean setIdleConnectionInPoolTimeoutInMs(int idleConnectionInPoolTimeoutInMs) {
        this.idleConnectionInPoolTimeoutInMs = idleConnectionInPoolTimeoutInMs;
        return this;
    }

    public AsyncHttpClientConfigBean setStrict302Handling(boolean strict302Handling) {
        this.strict302Handling = strict302Handling;
        return this;
    }

    public AsyncHttpClientConfigBean setIdleConnectionTimeoutInMs(int idleConnectionTimeoutInMs) {
        this.idleConnectionTimeoutInMs = idleConnectionTimeoutInMs;
        return this;
    }

    public AsyncHttpClientConfigBean setRequestTimeoutInMs(int requestTimeoutInMs) {
        this.requestTimeoutInMs = requestTimeoutInMs;
        return this;
    }

    public AsyncHttpClientConfigBean setMaxConnectionLifeTimeInMs(int maxConnectionLifeTimeInMs) {
        this.maxConnectionLifeTimeInMs = maxConnectionLifeTimeInMs;
        return this;
    }

    public AsyncHttpClientConfigBean setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
        return this;
    }

    public AsyncHttpClientConfigBean setMaxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
        return this;
    }

    public AsyncHttpClientConfigBean setCompressionEnabled(boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
        return this;
    }

    public AsyncHttpClientConfigBean setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public AsyncHttpClientConfigBean setAllowPoolingConnection(boolean allowPoolingConnection) {
        this.allowPoolingConnection = allowPoolingConnection;
        return this;
    }

    public AsyncHttpClientConfigBean setApplicationThreadPool(ExecutorService applicationThreadPool) {
        if (this.applicationThreadPool != null) {
            this.applicationThreadPool.shutdownNow();
        }
        this.applicationThreadPool = applicationThreadPool;
        return this;
    }

    public AsyncHttpClientConfigBean setProxyServer(ProxyServer proxyServer) {
        this.proxyServerSelector = ProxyUtils.createProxyServerSelector(proxyServer);
        return this;
    }

    public AsyncHttpClientConfigBean setProxyServerSelector(ProxyServerSelector proxyServerSelector) {
        this.proxyServerSelector = proxyServerSelector;
        return this;
    }

    public AsyncHttpClientConfigBean setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    public AsyncHttpClientConfigBean setProviderConfig(AsyncHttpProviderConfig<?, ?> providerConfig) {
        this.providerConfig = providerConfig;
        return this;
    }

    public AsyncHttpClientConfigBean setConnectionsPool(ConnectionsPool<?, ?> connectionsPool) {
        this.connectionsPool = connectionsPool;
        return this;
    }

    public AsyncHttpClientConfigBean setRealm(Realm realm) {
        this.realm = realm;
        return this;
    }

    public AsyncHttpClientConfigBean addRequestFilter(RequestFilter requestFilter) {
        requestFilters.add(requestFilter);
        return this;
    }

    public AsyncHttpClientConfigBean addResponseFilters(ResponseFilter responseFilter) {
        responseFilters.add(responseFilter);
        return this;
    }

    public AsyncHttpClientConfigBean addIoExceptionFilters(IOExceptionFilter ioExceptionFilter) {
        ioExceptionFilters.add(ioExceptionFilter);
        return this;
    }

    public AsyncHttpClientConfigBean setRequestCompressionLevel(int requestCompressionLevel) {
        this.requestCompressionLevel = requestCompressionLevel;
        return this;
    }

    public AsyncHttpClientConfigBean setMaxRequestRetry(int maxRequestRetry) {
        this.maxRequestRetry = maxRequestRetry;
        return this;
    }

    public AsyncHttpClientConfigBean setAllowSslConnectionPool(boolean allowSslConnectionPool) {
        this.allowSslConnectionPool = allowSslConnectionPool;
        return this;
    }

    public AsyncHttpClientConfigBean setDisableUrlEncodingForBoundedRequests(boolean disableUrlEncodingForBoundedRequests) {
        this.disableUrlEncodingForBoundedRequests = disableUrlEncodingForBoundedRequests;
        return this;
    }

    public AsyncHttpClientConfigBean setRemoveQueryParamOnRedirect(boolean removeQueryParamOnRedirect) {
        this.removeQueryParamOnRedirect = removeQueryParamOnRedirect;
        return this;
    }

    public AsyncHttpClientConfigBean setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    public AsyncHttpClientConfigBean setIoThreadMultiplier(int ioThreadMultiplier) {
        this.ioThreadMultiplier = ioThreadMultiplier;
        return this;
    }

    public AsyncHttpClientConfigBean setAcceptAnyCertificate(boolean acceptAnyCertificate) {
        this.acceptAnyCertificate = acceptAnyCertificate;
        return this;
    }
}
