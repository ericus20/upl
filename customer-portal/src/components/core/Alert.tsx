/* eslint-disable react/no-danger */
import AlertType from "enums/AlertType";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import { AlertBody, alertService } from "services/alert.service";

interface AlertProps {
  id?: string;
  fade?: boolean;
}

const Alert: React.FC<AlertProps> = ({ id, fade }) => {
  const router = useRouter();
  const [alerts, setAlerts] = useState<AlertBody[]>([]);

  const removeAlert = (alert: AlertBody) => {
    if (fade) {
      // fade out alert
      const alertWithFade = { ...alert, fade: true };
      setAlerts(previousAlerts =>
        previousAlerts.map(x => (x === alert ? alertWithFade : x))
      );

      // remove alert after faded out
      setTimeout(() => {
        setAlerts(previousAlerts =>
          previousAlerts.filter(x => x !== alertWithFade)
        );
      }, 250);
    } else {
      // remove alert
      setAlerts(previousAlerts => previousAlerts.filter(x => x !== alert));
    }
  };

  useEffect(() => {
    // subscribe to new alert notifications
    const subscription = alertService.onAlert(id).subscribe(alert => {
      // clear alerts when an empty alert is received
      if (!alert.message) {
        setAlerts(thisAlerts => {
          // filter out alerts without 'keepAfterRouteChange' flag
          const filteredAlerts = thisAlerts.filter(x => x.keepAfterRouteChange);

          // set 'keepAfterRouteChange' flag to false on the rest
          // eslint-disable-next-line no-param-reassign
          filteredAlerts.forEach(x => delete x.keepAfterRouteChange);
          return filteredAlerts;
        });
      } else {
        // add alert to array
        setAlerts(previousAlerts => [...previousAlerts, alert]);

        // auto close alert if required
        if (alert.autoClose) {
          setTimeout(() => removeAlert(alert), 3000);
        }
      }
    });

    // clear alerts on location change
    const clearAlerts = () => {
      setTimeout(() => alertService.clear(id));
    };
    router.events.on("routeChangeStart", clearAlerts);

    // clean up function that runs when the component unmounts
    return () => {
      // unsubscribe to avoid memory leaks
      subscription.unsubscribe();
      router.events.off("routeChangeStart", clearAlerts);
    };

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const cssClasses = (alert: AlertBody): string => {
    if (!alert) return "";

    const classes = [
      "bg-teal-100 border-t-4 border-teal-500 rounded-b text-teal-900 px-4 py-3 shadow-md",
    ];

    const alertTypeClass = {
      [AlertType.SUCCESS]: "bg-teal-500 border-teal-400",
      [AlertType.ERROR]: "bg-red-500 border-red-400",
      [AlertType.INFO]: "bg-blue-100 border-blue-500 text-blue-700",
      [AlertType.WARNING]: "bg-orange-100 border-orange-500 text-orange-700",
    };

    if (alert.type) {
      classes.push(alertTypeClass[alert.type]);
    }

    if (alert.fade) {
      classes.push("fade");
    }

    return classes.join(" ");
  };

  if (!alerts.length) return null;

  return (
    <div
      className="bg-teal-100 border-t-4 border-teal-500 rounded-b text-teal-900 px-4 py-3 shadow-md"
      role="alert"
    >
      <div className="flex">
        <div className="py-1">
          <svg
            className="fill-current h-6 w-6 text-teal-500 mr-4"
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 20 20"
          >
            <path d="M2.93 17.07A10 10 0 1 1 17.07 2.93 10 10 0 0 1 2.93 17.07zm12.73-1.41A8 8 0 1 0 4.34 4.34a8 8 0 0 0 11.32 11.32zM9 11V9h2v6H9v-4zm0-6h2v2H9V5z" />
          </svg>
        </div>
        <div>
          {alerts.map(alert => (
            <div key={alert.id} className={cssClasses(alert)}>
              <button
                className="close"
                type="button"
                onClick={() => removeAlert(alert)}
              >
                &times;
              </button>
              {alert.message && (
                <span dangerouslySetInnerHTML={{ __html: alert.message }} />
              )}
            </div>
          ))}

          <p className="font-bold">Our privacy policy has changed</p>
          <p className="text-sm">
            Make sure you know how these changes affect you.
          </p>
        </div>
      </div>
    </div>
  );
};

Alert.defaultProps = {
  id: "default-alert",
  fade: false,
};

export default Alert;
