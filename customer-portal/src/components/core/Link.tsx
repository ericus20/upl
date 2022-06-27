import NextLink, { LinkProps as ExternalLinkProps } from "next/link";

interface LinkProps extends ExternalLinkProps {
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
