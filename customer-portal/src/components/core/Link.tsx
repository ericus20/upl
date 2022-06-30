import NextLink from "next/link";
import { AnchorHTMLAttributes } from "react";

interface LinkProps extends AnchorHTMLAttributes<HTMLAnchorElement> {
  href: string;
  children?: React.ReactNode; // best, accepts everything React can render
}

const Link: React.FC<LinkProps> = ({ children, href, ...props }) => {
  return (
    <NextLink href={href}>
      <a {...props}>{children}</a>
    </NextLink>
  );
};

export default Link;
