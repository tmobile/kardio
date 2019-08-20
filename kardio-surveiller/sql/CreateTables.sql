CREATE DATABASE  IF NOT EXISTS `kardio_oss` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `kardio_oss`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win64 (x86_64)
--
-- Host:    Database: kardio
-- ------------------------------------------------------
-- Server version	5.6.40-log

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
-- Table structure for table `alert_subscription`
--

DROP TABLE IF EXISTS `alert_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alert_subscription` (
  `alert_subscription_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_id` int(11) DEFAULT NULL,
  `auth_token` varchar(45) DEFAULT NULL,
  `subscription_val` varchar(100) NOT NULL,
  `environment_id` int(11) NOT NULL,
  `validation_level` int(11) DEFAULT NULL,
  `subscription_type` int(11) NOT NULL,
  `global_component_type_id` int(11) DEFAULT NULL,
  `platform` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`alert_subscription_id`),
  KEY `fk_alert_subscription_component1_idx` (`component_id`),
  KEY `idx_auth_token` (`auth_token`),
  KEY `idx_environment` (`environment_id`),
  CONSTRAINT `fk_alert_subscription_component1` FOREIGN KEY (`component_id`) REFERENCES `component` (`component_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_alert_subscription_environment1` FOREIGN KEY (`environment_id`) REFERENCES `environment` (`environment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=405 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `api_status`
--

DROP TABLE IF EXISTS `api_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_status` (
  `api_status_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_id` int(11) NOT NULL,
  `environment_id` int(11) NOT NULL,
  `total_api` int(11) NOT NULL,
  `delta_value` int(11) NOT NULL,
  `status_date` date NOT NULL,
  PRIMARY KEY (`api_status_id`),
  KEY `component_id_idx` (`component_id`),
  KEY `environment_id_idx` (`environment_id`),
  KEY `stats_date_index` (`status_date`),
  CONSTRAINT `com_id` FOREIGN KEY (`component_id`) REFERENCES `component` (`component_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `env_id` FOREIGN KEY (`environment_id`) REFERENCES `environment` (`environment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=56650 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_lookup`
--

DROP TABLE IF EXISTS `app_lookup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_lookup` (
  `app_lookup_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_full_name` varchar(200) NOT NULL,
  `component_id` int(11) NOT NULL,
  PRIMARY KEY (`app_lookup_id`),
  KEY `fk_app_lookup_component1_idx` (`component_id`),
  CONSTRAINT `fk_app_lookup_component1` FOREIGN KEY (`component_id`) REFERENCES `component` (`component_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_role`
--

DROP TABLE IF EXISTS `app_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_role` (
  `app_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_id` int(11) DEFAULT NULL,
  `app_role_name` varchar(100) NOT NULL,
  PRIMARY KEY (`app_role_id`),
  KEY `app_id_idx` (`component_id`),
  CONSTRAINT `app_id` FOREIGN KEY (`component_id`) REFERENCES `component` (`component_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_session`
--

DROP TABLE IF EXISTS `app_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_session` (
  `app_session_id` int(11) NOT NULL AUTO_INCREMENT,
  `auth_token` varchar(45) NOT NULL,
  `session_start_time` datetime NOT NULL,
  `userid` varchar(45) DEFAULT NULL,
  `user_name` varchar(45) DEFAULT NULL,
  `permission` varchar(16384) DEFAULT NULL,
  `is_admin` int(1) DEFAULT NULL,
  PRIMARY KEY (`app_session_id`)
) ENGINE=InnoDB AUTO_INCREMENT=480 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audit`
--

DROP TABLE IF EXISTS `audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit` (
  `audit_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(45) DEFAULT NULL,
  `audit_log` varchar(1000) DEFAULT NULL,
  `audit_date` datetime DEFAULT NULL,
  PRIMARY KEY (`audit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comp_failure_log`
--

DROP TABLE IF EXISTS `comp_failure_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comp_failure_log` (
  `comp_failure_log_id` int(11) NOT NULL AUTO_INCREMENT,
  `health_check_id` int(11) NOT NULL,
  `status_id` int(11) NOT NULL,
  `comp_reg_sts_time` datetime NOT NULL,
  `failure_message` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`comp_failure_log_id`),
  KEY `status_id_idx` (`status_id`),
  KEY `fk_comp_reg_status_health_check1_idx` (`health_check_id`),
  KEY `comp_reg_sts_time_index` (`comp_reg_sts_time`),
  CONSTRAINT `fk_comp_reg_status_health_check1` FOREIGN KEY (`health_check_id`) REFERENCES `health_check` (`health_check_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `status_id` FOREIGN KEY (`status_id`) REFERENCES `status` (`status_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=748060 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `component`
--

DROP TABLE IF EXISTS `component`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `component` (
  `component_id` int(11) NOT NULL AUTO_INCREMENT,
  `comp_name` varchar(300) NOT NULL,
  `comp_desc` varchar(300) DEFAULT NULL,
  `parent_component_id` int(11) DEFAULT NULL,
  `component_type_id` int(11) NOT NULL,
  `del_ind` tinyint(1) NOT NULL,
  `platform` varchar(45) DEFAULT NULL,
  `manual` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`component_id`),
  KEY `fk_component_component1_idx` (`parent_component_id`),
  KEY `fk_component_component_type1_idx` (`component_type_id`),
  KEY `comp_name_idx` (`comp_name`),
  CONSTRAINT `fk_component_component1` FOREIGN KEY (`parent_component_id`) REFERENCES `component` (`component_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_component_component_type1` FOREIGN KEY (`component_type_id`) REFERENCES `component_type` (`component_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15894 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `component_message`
--

DROP TABLE IF EXISTS `component_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `component_message` (
  `component_message_id` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(1000) NOT NULL,
  `user_id` varchar(45) DEFAULT NULL,
  `user_name` varchar(45) DEFAULT NULL,
  `component_id` int(11) DEFAULT NULL,
  `region_id` int(11) NOT NULL,
  `environment_id` int(11) NOT NULL,
  `message_date` datetime NOT NULL,
  PRIMARY KEY (`component_message_id`),
  KEY `comp_id_idx` (`component_id`),
  KEY `region_id_idx` (`region_id`),
  KEY `environment_id_idx` (`environment_id`),
  CONSTRAINT `comp_id` FOREIGN KEY (`component_id`) REFERENCES `component` (`component_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `environment_id` FOREIGN KEY (`environment_id`) REFERENCES `environment` (`environment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `region_id` FOREIGN KEY (`region_id`) REFERENCES `region` (`region_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `component_type`
--

DROP TABLE IF EXISTS `component_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `component_type` (
  `component_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_type_name` varchar(45) DEFAULT NULL,
  `component_type_desc` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`component_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `container_stats`
--

DROP TABLE IF EXISTS `container_stats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `container_stats` (
  `container_stats_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_id` int(11) NOT NULL,
  `environment_id` int(11) NOT NULL,
  `stats_date` date NOT NULL,
  `total_container` int(11) NOT NULL,
  `delta_value` int(11) NOT NULL,
  PRIMARY KEY (`container_stats_id`),
  KEY `fk_container_stats_component1_idx` (`component_id`),
  KEY `fk_container_stats_environment1_idx` (`environment_id`),
  KEY `stats_date_index` (`stats_date`),
  CONSTRAINT `fk_container_stats_component1` FOREIGN KEY (`component_id`) REFERENCES `component` (`component_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_container_stats_environment1` FOREIGN KEY (`environment_id`) REFERENCES `environment` (`environment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1616557 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `counter`
--

DROP TABLE IF EXISTS `counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `counter` (
  `counter_id` int(11) NOT NULL AUTO_INCREMENT,
  `counter_name` varchar(45) NOT NULL,
  `counter_desc` varchar(256) NOT NULL,
  `position` int(11) NOT NULL,
  `del_ind` int(11) DEFAULT NULL,
  PRIMARY KEY (`counter_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `counter_metric`
--

DROP TABLE IF EXISTS `counter_metric`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `counter_metric` (
  `counter_metric_id` int(11) NOT NULL AUTO_INCREMENT,
  `env_counter_id` int(11) NOT NULL,
  `metric_val` float NOT NULL,
  `metric_date` datetime NOT NULL,
  PRIMARY KEY (`counter_metric_id`),
  KEY `counter_param_id_idx` (`env_counter_id`),
  KEY `idx_metric_date` (`metric_date`),
  CONSTRAINT `counter_param_id` FOREIGN KEY (`env_counter_id`) REFERENCES `env_counter` (`env_counter_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4792328 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `counter_metric_history`
--

DROP TABLE IF EXISTS `counter_metric_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `counter_metric_history` (
  `counter_metric_history_id` int(11) NOT NULL AUTO_INCREMENT,
  `env_counter_id` int(11) NOT NULL,
  `metric_val` float NOT NULL,
  `metric_date` datetime NOT NULL,
  PRIMARY KEY (`counter_metric_history_id`),
  KEY `env_counter_id_idx` (`env_counter_id`),
  CONSTRAINT `env_counter_id` FOREIGN KEY (`env_counter_id`) REFERENCES `env_counter` (`env_counter_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2111183 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `counter_metric_type`
--

DROP TABLE IF EXISTS `counter_metric_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `counter_metric_type` (
  `counter_metric_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `counter_metric_type` varchar(45) DEFAULT NULL,
  `counter_metric_type_desc` varchar(100) DEFAULT NULL,
  `counter_metric_type_class_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`counter_metric_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dailly_comp_status`
--

DROP TABLE IF EXISTS `dailly_comp_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dailly_comp_status` (
  `dailly_comp_status_id` int(11) NOT NULL AUTO_INCREMENT,
  `health_check_id` int(11) NOT NULL,
  `status_id` int(11) NOT NULL,
  `status_date` date NOT NULL,
  `percentage_up_time` decimal(5,2) DEFAULT NULL,
  `total_failure_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`dailly_comp_status_id`),
  KEY `fk_dailly_comp_status_status1_idx` (`status_id`),
  KEY `fk_dailly_comp_status_health_check1_idx` (`health_check_id`),
  KEY `idx_dailly_comp_status_status_date_health_check_id` (`status_date`,`health_check_id`),
  CONSTRAINT `fk_dailly_comp_status_health_check1` FOREIGN KEY (`health_check_id`) REFERENCES `health_check` (`health_check_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_dailly_comp_status_status1` FOREIGN KEY (`status_id`) REFERENCES `status` (`status_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2049023 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `env_counter`
--

DROP TABLE IF EXISTS `env_counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `env_counter` (
  `env_counter_id` int(11) NOT NULL AUTO_INCREMENT,
  `environment_id` int(11) DEFAULT NULL,
  `counter_id` int(11) NOT NULL,
  `counter_metric_type_id` int(11) NOT NULL,
  `parameter_1` varchar(1000) DEFAULT NULL,
  `parameter_2` varchar(1000) DEFAULT NULL,
  `platform` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`env_counter_id`),
  KEY `counter_metric_type_id_idx` (`counter_metric_type_id`),
  KEY `env_counter_environment_id_idx` (`environment_id`),
  KEY `env_counter_counter_id_idx` (`counter_id`),
  CONSTRAINT `env_counter_counter_id` FOREIGN KEY (`counter_id`) REFERENCES `counter` (`counter_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `env_counter_counter_metric_type_id` FOREIGN KEY (`counter_metric_type_id`) REFERENCES `counter_metric_type` (`counter_metric_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `env_counter_environment_id` FOREIGN KEY (`environment_id`) REFERENCES `environment` (`environment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `environment`
--

DROP TABLE IF EXISTS `environment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `environment` (
  `environment_id` int(11) NOT NULL AUTO_INCREMENT,
  `environment_name` varchar(45) NOT NULL,
  `environment_desc` varchar(300) DEFAULT NULL,
  `general_message` varchar(1000) DEFAULT NULL,
  `app_message` varchar(1000) DEFAULT NULL,
  `infra_message` varchar(1000) DEFAULT NULL,
  `marathon_url` varchar(300) DEFAULT NULL,
  `marathon_json` longblob,
  `last_updated_time` datetime DEFAULT NULL,
  `environment_lock` int(11) DEFAULT '0',
  `display_order` int(11) DEFAULT NULL,
  `marathon_cred` varchar(1000) DEFAULT NULL,
  `k8s_url` varchar(1000) DEFAULT NULL,
  `k8s_cred` varchar(1000) DEFAULT NULL,
  `east_marathon_url` varchar(1000) DEFAULT NULL,
  `east_marathon_json` longblob,
  `east_last_updated_time` datetime DEFAULT NULL,
  `k8s_tps_query` varchar(1000) DEFAULT NULL,
  `k8s_latency_query` varchar(1000) DEFAULT NULL,
  `mesos_tps_query` varchar(1000) DEFAULT NULL,
  `mesos_latency_query` varchar(1000) DEFAULT NULL,
  `counter_message` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`environment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `health_check`
--

DROP TABLE IF EXISTS `health_check`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `health_check` (
  `health_check_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_id` int(11) NOT NULL,
  `region_id` int(11) NOT NULL,
  `environment_id` int(11) NOT NULL,
  `health_check_type_id` int(11) NOT NULL,
  `max_retry_count` int(11) NOT NULL,
  `failed_count` int(11) DEFAULT NULL,
  `current_status_id` int(11) DEFAULT NULL,
  `status_update_time` datetime DEFAULT NULL,
  `last_status_change` datetime DEFAULT NULL,
  `del_ind` tinyint(1) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`health_check_id`),
  UNIQUE KEY `health_check_id_UNIQUE` (`health_check_id`),
  KEY `fk_health_check_health_check_type1_idx` (`health_check_type_id`),
  KEY `fk_health_check_component1_idx` (`component_id`),
  KEY `fk_health_check_region1_idx` (`region_id`),
  KEY `fk_health_check_status1_idx` (`current_status_id`),
  KEY `fk_health_check_environment1_idx` (`environment_id`),
  KEY `last_status_changed_index` (`last_status_change`),
  KEY `idx_health_check_health_check_id_environment_id` (`health_check_id`,`environment_id`),
  CONSTRAINT `fk_health_check_component1` FOREIGN KEY (`component_id`) REFERENCES `component` (`component_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_health_check_environment1` FOREIGN KEY (`environment_id`) REFERENCES `environment` (`environment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_health_check_health_check_type1` FOREIGN KEY (`health_check_type_id`) REFERENCES `health_check_type` (`health_check_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_health_check_region1` FOREIGN KEY (`region_id`) REFERENCES `region` (`region_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_health_check_status1` FOREIGN KEY (`current_status_id`) REFERENCES `status` (`status_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16875 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `health_check_param`
--

DROP TABLE IF EXISTS `health_check_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `health_check_param` (
  `health_check_param_id` int(11) NOT NULL AUTO_INCREMENT,
  `health_check_id` int(11) NOT NULL,
  `health_check_param_key` varchar(45) NOT NULL,
  `health_check_param_val` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`health_check_param_id`),
  UNIQUE KEY `health_check_param_id_UNIQUE` (`health_check_param_id`),
  KEY `fk_health_check_param_health_check1_idx` (`health_check_id`) USING BTREE,
  CONSTRAINT `fk_health_check_param_health_check` FOREIGN KEY (`health_check_id`) REFERENCES `health_check` (`health_check_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=20801957 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `health_check_type`
--

DROP TABLE IF EXISTS `health_check_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `health_check_type` (
  `health_check_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `health_check_type_name` varchar(45) NOT NULL,
  `health_check_type_desc` varchar(300) NOT NULL,
  `health_check_class_name` varchar(300) NOT NULL,
  PRIMARY KEY (`health_check_type_id`),
  UNIQUE KEY `health_check_type_id_UNIQUE` (`health_check_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `k8s_api_status`
--

DROP TABLE IF EXISTS `k8s_api_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `k8s_api_status` (
  `k8s_api_status_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_id` int(11) NOT NULL,
  `environment_id` int(11) NOT NULL,
  `total_api` int(11) NOT NULL,
  `status_date` date NOT NULL,
  PRIMARY KEY (`k8s_api_status_id`),
  KEY `component_id_idx` (`component_id`),
  KEY `environment_id_idx` (`environment_id`),
  CONSTRAINT `component_id` FOREIGN KEY (`component_id`) REFERENCES `component` (`component_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `environment_id1` FOREIGN KEY (`environment_id`) REFERENCES `environment` (`environment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=53688 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `k8s_obj_pods`
--

DROP TABLE IF EXISTS `k8s_obj_pods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `k8s_obj_pods` (
  `k8s_obj_pods_id` int(11) NOT NULL AUTO_INCREMENT,
  `obj_name` varchar(200) DEFAULT NULL,
  `pods` int(4) NOT NULL,
  `containers` int(4) NOT NULL,
  `environment_id` int(4) NOT NULL,
  `status_date` date NOT NULL,
  PRIMARY KEY (`k8s_obj_pods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1618 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `k8s_pods_containers`
--

DROP TABLE IF EXISTS `k8s_pods_containers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `k8s_pods_containers` (
  `k8s_pods_containers_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_id` int(11) NOT NULL,
  `environment_id` int(11) NOT NULL,
  `total_pods` int(11) DEFAULT NULL,
  `status_date` date DEFAULT NULL,
  `total_containers` int(11) NOT NULL,
  PRIMARY KEY (`k8s_pods_containers_id`)
) ENGINE=InnoDB AUTO_INCREMENT=276812 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `k8s_tps_latency_history`
--

DROP TABLE IF EXISTS `k8s_tps_latency_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `k8s_tps_latency_history` (
  `k8s_tps_latency_history_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_id` int(11) NOT NULL,
  `environment_id` int(11) NOT NULL,
  `tps_value` decimal(10,2) DEFAULT NULL,
  `status_date` date DEFAULT NULL,
  `latency_value` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`k8s_tps_latency_history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=182528 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prom_lookup`
--

DROP TABLE IF EXISTS `prom_lookup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prom_lookup` (
  `prom_lookup_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_id` int(11) NOT NULL,
  `http_path` varchar(1000) DEFAULT NULL,
  `launch_date` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `environment_id` int(11) NOT NULL,
  PRIMARY KEY (`prom_lookup_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17484 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `region` (
  `region_id` int(11) NOT NULL AUTO_INCREMENT,
  `region_name` varchar(45) NOT NULL,
  `region_desc` varchar(300) DEFAULT NULL,
  `region_lock` int(11) DEFAULT '0',
  PRIMARY KEY (`region_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `status_id` int(11) NOT NULL AUTO_INCREMENT,
  `status_name` varchar(45) NOT NULL,
  `status_desc` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`status_id`),
  UNIQUE KEY `status_id_UNIQUE` (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `surv_watch`
--

DROP TABLE IF EXISTS `surv_watch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `surv_watch` (
  `surv_watch_id` int(11) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(100) NOT NULL,
  `last_init` datetime DEFAULT NULL,
  `health_check_started` datetime DEFAULT NULL,
  `health_check_finished` datetime DEFAULT NULL,
  PRIMARY KEY (`surv_watch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `surveillance_resource_type`
--

DROP TABLE IF EXISTS `surveillance_resource_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `surveillance_resource_type` (
  `surveillance_resource_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `resource_type_name` varchar(256) NOT NULL,
  `resource_type_description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`surveillance_resource_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `surveillance_rule`
--

DROP TABLE IF EXISTS `surveillance_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `surveillance_rule` (
  `surveillance_rule_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `check_interval_ms` bigint(20) NOT NULL,
  `surveillance_resource_type_id` int(11) NOT NULL,
  `param_json` varchar(4096) NOT NULL,
  PRIMARY KEY (`surveillance_rule_id`),
  KEY `fk_surveillance_rule_surveillance_resource_type1_idx` (`surveillance_resource_type_id`),
  CONSTRAINT `fk_surveillance_rule_surveillance_resource_type1` FOREIGN KEY (`surveillance_resource_type_id`) REFERENCES `surveillance_resource_type` (`surveillance_resource_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `surveillance_rule_status`
--

DROP TABLE IF EXISTS `surveillance_rule_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `surveillance_rule_status` (
  `surveillance_rule_status_id` int(11) NOT NULL AUTO_INCREMENT,
  `surveillance_rule_id` int(11) NOT NULL,
  `last_run_time` datetime NOT NULL,
  `next_scheduled_run_time` datetime NOT NULL,
  PRIMARY KEY (`surveillance_rule_status_id`),
  KEY `fk_surveillance_rule_status_surveillance_rule1_idx` (`surveillance_rule_id`),
  CONSTRAINT `fk_surveillance_rule_status_surveillance_rule1` FOREIGN KEY (`surveillance_rule_id`) REFERENCES `surveillance_rule` (`surveillance_rule_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tps_latency_history`
--

DROP TABLE IF EXISTS `tps_latency_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tps_latency_history` (
  `tps_latency_history_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_id` int(11) NOT NULL,
  `environment_id` int(11) NOT NULL,
  `tps_value` decimal(10,2) DEFAULT NULL,
  `status_date` date DEFAULT NULL,
  `latency_value` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`tps_latency_history_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1341889 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tps_service`
--

DROP TABLE IF EXISTS `tps_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tps_service` (
  `tps_service_id` int(11) NOT NULL AUTO_INCREMENT,
  `component_id` int(11) NOT NULL,
  `environment_id` int(11) NOT NULL,
  `tps_value` decimal(10,2) DEFAULT NULL,
  `launch_date` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `latency_value` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`tps_service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15966 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-08-19 17:04:47
