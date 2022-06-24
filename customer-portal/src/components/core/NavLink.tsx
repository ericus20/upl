import classnames from "classnames";
import { useRouter } from "next/router";
import React from "react";
import Link from "./Link";

interface NavLinkProps extends React.AnchorHTMLAttributes<HTMLAnchorElement> {
  href: string;
  exact: boolean;
  children?: React.ReactNode; // best, accepts everything React can render
}

const NavLink: React.FC<NavLinkProps> = ({
  href,
  exact,
  children,
  ...props
}) => {
  const { pathname } = useRouter();

  const classNames = classnames(props?.className, {
    active: exact ? pathname === href : pathname.startsWith(href),
  });

  return (
    <Link href={href} className={classNames} {...props}>
      {children}
    </Link>
  );
};

NavLink.defaultProps = {
  children: null,
};

export default NavLink;
