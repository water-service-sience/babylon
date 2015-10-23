This is your new Play 2.1 application
=====================================


### CentOSでlocalhostが解決できない場合

```
ip address show
```
で、PrivateIPを調べる。
また、

```
echo $HOSTNAME
```
でホスト名を調べる。
そして、
```
sudo vim /etc/hosts
```
の１行目に
```
PRIVATE_IP HOSTNAME localhost
```
を設定して、javaを起動し直すと解決できます。


### Server contextの設定

server contextは/babylonとする。

apache(/etc/httpd/conf/httpd.conf)に以下の設定を追加し、プロキシを有効化。

```
NameVirtualHost *:80
<VirtualHost *:80>
  ProxyPreserveHost On
  ProxyPass /babylon http://127.0.0.1:9000/babylon
  ProxyPassReverse /babylon http://127.0.0.1:9000/babylon
</VirtualHost>

```

{project}/babylon-server/app/application.confに


```
application.context="/babylon"
```

を追加。


