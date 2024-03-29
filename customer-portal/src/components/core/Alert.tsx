/* eslint-disable react/no-danger */
import AlertType from "enums/AlertType";
import { useRouter } from "next/router";
import React, { useEffect, useRef, useState } from "react";
import { AlertBody, alertService } from "services/alert.service";
import { v4 as uuid } from "uuid";

interface AlertProps {
  id?: string;
  fade?: boolean;
  className?: string;
}

const Alert: React.FC<AlertProps> = ({ id, fade, className }) => {
  const router = useRouter();
  const mounted = useRef<boolean>(false);
  const [alerts, setAlerts] = useState<AlertBody[]>([]);

  const removeAlert = (alert: AlertBody) => {
    if (!mounted.current) return;

    if (fade) {
      // fade out alert
      const alertWithFade: AlertBody = { ...alert, fade: true };
      setAlerts(previousAlerts =>
        previousAlerts.map(x => (x.id === alert.id ? alertWithFade : x))
      );

      // remove alert after faded out
      setTimeout(() => {
        setAlerts(previousAlerts =>
          previousAlerts.filter(x => x.id !== alertWithFade.id)
        );
      }, 250);
    } else {
      // remove alert
      setAlerts(previousAlerts =>
        previousAlerts.filter(x => x.id !== alert.id)
      );
    }
  };

  const omit = (arr: AlertBody[], key: keyof AlertBody): AlertBody[] => {
    return arr.map(obj => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const { [key]: omitted, ...rest } = obj;
      return rest;
    });
  };

  useEffect(() => {
    mounted.current = true;

    // subscribe to new alert notifications
    const subscription = alertService.onAlert(id).subscribe(alert => {
      // clear alerts when an empty alert is received
      if (!alert.message) {
        setAlerts(thisAlerts => {
          // filter out alerts without 'keepAfterRouteChange' flag
          const filteredAlerts = thisAlerts.filter(x => x.keepAfterRouteChange);

          // remove 'keepAfterRouteChange' flag on the rest
          return omit(filteredAlerts, "keepAfterRouteChange");
        });
      } else {
        // add alert to array
        setAlerts(previousAlerts => [...previousAlerts, alert]);

        // auto close alert if required
        if (alert.autoClose) {
          setTimeout(() => removeAlert(alert), 5000);
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
      mounted.current = false;

      // unsubscribe to avoid memory leaks
      subscription.unsubscribe();
      router.events.off("routeChangeStart", clearAlerts);
    };

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const cssClasses = (alert: AlertBody): string => {
    if (!alert) return "";

    const classes = ["rounded-b px-4 py-3 shadow-md border-l-4 items-center"];

    const alertTypeClass = {
      [AlertType.Success]: "bg-teal-100 border-teal-500 text-teal-700",
      [AlertType.Error]: "bg-red-100 border-red-500 text-red-700",
      [AlertType.Info]: "flex bg-blue-100 border-blue-500 text-blue-700",
      [AlertType.Warning]: "bg-orange-100 border-orange-500 text-orange-700",
    };

    if (alert.type) {
      classes.push(alertTypeClass[alert.type]);
    }

    if (fade || alert.fade) {
      classes.push("animate-fade-in animate-fade-out");
    }

    return classes.join(" ");
  };

  if (!alerts.length) return null;

  return (
    <div role="alert" className={className}>
      {alerts.map(alert => (
        <div
          key={uuid()}
          className={`relative rounded mb-4 p-3 ${cssClasses(alert)}`}
        >
          {alert.type === AlertType.Info && (
            <div>
              <svg
                className="fill-current h-6 w-6 text-blue-500 mr-4"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 20 20"
              >
                <path d="M2.93 17.07A10 10 0 1 1 17.07 2.93 10 10 0 0 1 2.93 17.07zm12.73-1.41A8 8 0 1 0 4.34 4.34a8 8 0 0 0 11.32 11.32zM9 11V9h2v6H9v-4zm0-6h2v2H9V5z" />
              </svg>
            </div>
          )}

          <div className="flex flex-grow items-center justify-between">
            {alert.message && (
              <span
                className="text-center"
                dangerouslySetInnerHTML={{ __html: alert.message }}
              />
            )}
            <button
              type="button"
              className="text-black shadow-md text-2xl float-right opacity-30 bg-transparent cursor-pointer"
              onClick={() => removeAlert(alert)}
            >
              &times;
            </button>
          </div>
        </div>
      ))}
    </div>
  );
};

Alert.defaultProps = {
  id: "default-alert",
  fade: false,
  className: "my-1",
};

export default Alert;
