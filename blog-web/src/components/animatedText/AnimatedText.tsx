import React from "react";
import styles from "./animatedText.module.css";

interface AnimatedTextProps {
  children: React.ReactNode;
  className?: string;
}

const AnimatedText: React.FC<AnimatedTextProps> = ({ children, className = "" }) => {
  return (
    <span className={`${styles.animated} ${className}`.trim()}>{children}</span>
  );
};

export default AnimatedText;
