-- MySQL dump 10.13  Distrib 5.6.26, for osx10.10 (x86_64)
--
-- Host: localhost    Database: babylon
-- ------------------------------------------------------
-- Server version	5.6.26

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `fieldrouter`
--

LOCK TABLES `fieldrouter` WRITE;
/*!40000 ALTER TABLE `fieldrouter` DISABLE KEYS */;
INSERT INTO `fieldrouter` VALUES (1,136.8725,34.907167,'vbox0094','2014-08-13 16:39:48','半田','HandaMet'),(2,140.218666666667,35.7318333333333,'vbox0115','2014-08-13 16:39:50','印旛沼1','inba1'),(3,140.205166666667,35.7423333333333,'vbox0116','2014-08-13 16:39:52','印旛沼2','inba2');
/*!40000 ALTER TABLE `fieldrouter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `contacttype`
--

LOCK TABLES `contacttype` WRITE;
/*!40000 ALTER TABLE `contacttype` DISABLE KEYS */;
INSERT INTO `contacttype` VALUES ('電話番号',1),('住所',2),('E-mail',3),('携帯電話',4);
/*!40000 ALTER TABLE `contacttype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `displayfielddata`
--

LOCK TABLES `displayfielddata` WRITE;
/*!40000 ALTER TABLE `displayfielddata` DISABLE KEYS */;
INSERT INTO `displayfielddata` VALUES (1,1,'Timestamp','撮影日時','0',NULL,'1','度'),(2,1,'Port2','気温',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `displayfielddata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `postcategory`
--

LOCK TABLES `postcategory` WRITE;
/*!40000 ALTER TABLE `postcategory` DISABLE KEYS */;
INSERT INTO `postcategory` VALUES (0,1,'作業記録'),(0,2,'公開'),(0,3,'問い合わせ'),(3,4,'漏水'),(0,5,'その他'),(0,6,'会場'),(0,7,'いきもの');
/*!40000 ALTER TABLE `postcategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `waterlevelfield`
--

LOCK TABLES `waterlevelfield` WRITE;
/*!40000 ALTER TABLE `waterlevelfield` DISABLE KEYS */;
INSERT INTO `waterlevelfield` VALUES (49.705,1,'Port5','Timestamp',1,-0.0317,'HandaMet','HandaMet','Millivolt[mV]'),(51.787,2,'Port2','Timestamp',1,-0.0343,'HandaW1','HandaW1','Millivolt[mV]'),(49.682,3,'Port2','Timestamp',1,-0.0318,'HandaW2','HandaW2','Millivolt[mV]'),(47.065,21,'Port5','Timestamp',2,-0.0302,'inba1','inba1','Millivolt[mV]'),(42.287,22,'Port3','Timestamp',2,-0.0243,'inbaW1','inbaW1','Millivolt[mV]'),(48.743,31,'Port5','Timestamp',3,-0.0297,'inba2','inba2','Millivolt[mV]'),(69.255,32,'Port3','Timestamp',3,-0.0433,'inbaW2','inbaW2','Millivolt[mV]');
/*!40000 ALTER TABLE `waterlevelfield` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `poststatus`
--

LOCK TABLES `poststatus` WRITE;
/*!40000 ALTER TABLE `poststatus` DISABLE KEYS */;
INSERT INTO `poststatus` VALUES (1,'Not start','2'),(2,'progress','');
/*!40000 ALTER TABLE `poststatus` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-10-24  3:26:54
