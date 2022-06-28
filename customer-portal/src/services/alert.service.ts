import AlertType from "enums/AlertType";
import { Subject } from "rxjs";
import { filter } from "rxjs/operators";

export interface AlertBody {
  id?: string;
  message?: string;
  autoClose?: boolean;
  type?: AlertType;
  fade?: boolean;
  keepAfterRouteChange?: boolean;
}

const alertSubject = new Subject<AlertBody>();
const defaultId = "default-alert";

// enable subscribing to alerts observable
const onAlert = (id = defaultId) => {
  return alertSubject.asObservable().pipe(filter(x => x && x.id === id));
};

// core alert method
const sendAlert = (alert: AlertBody) => {
  alertSubject.next({
    id: alert.id || defaultId,
    autoClose: alert.autoClose === undefined ? true : alert.autoClose,
    ...alert,
  });
};

// convenience methods
const success = (message: string, options: AlertBody) => {
  sendAlert({ ...options, type: AlertType.Success, message });
};

const error = (message: string, options: AlertBody) => {
  sendAlert({ ...options, type: AlertType.Error, message });
};

const info = (message: string, options: AlertBody) => {
  sendAlert({ ...options, type: AlertType.Info, message });
};

const warn = (message: string, options: AlertBody) => {
  sendAlert({ ...options, type: AlertType.Warning, message });
};

// clear alerts
const clear = (id = defaultId) => {
  alertSubject.next({ id });
};

export const alertService = {
  onAlert,
  success,
  error,
  info,
  warn,
  alert: sendAlert,
  clear,
};
