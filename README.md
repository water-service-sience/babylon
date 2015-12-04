
# 実行

## 環境構築

### Java

OracleJDK Java8 以上と、

### SBT

最新の[SBT](http://www.scala-sbt.org/)を実行できるようにしておいてください。

### データベース

MySQL 5.5以上を入れてください。

MySQLでは、

    create database babylon;

でデータベースを作り、ローカルホスト(サーバーとMySQLを同じサーバーで動かす場合)からユーザー名 babylon パスワードbabylonでアクセスできるように権限を追加してください。

## ビルド+実行

環境構築が完了したら、

    sbt run

を実行してください。これだけでビルド、起動、データベースへのテーブル作成が行われます。


## 本番系での実行

現在は、サーバーで

   sbt start

で実行しています。



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


