DELETE FROM app_session WHERE datediff(now(), session_start_time) > 7;
DELETE FROM counter_metric where datediff(now(), metric_date) > 7;