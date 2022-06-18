import NextLink from "next/link";

interface LinkProps {
  href: string;
  children?: React.ReactNode; // best, accepts everything React can render
  props?: React.AnchorHTMLAttributes<HTMLAnchorElement>;
}

const Link: React.FC<LinkProps> = ({ href, children, props }) => {
  return (
    <NextLink href={href}>
      <a {...props}>{children}</a>
    </NextLink>
  );
};

export default Link;
