import React from "react";
import styles from "./button.module.css";

interface ButtonProps {
    onClick: () => void;
    children: React.ReactNode;
    className?: string;
    disabled?: boolean;
    ariaLabel?: string;
}

const Button: React.FC<ButtonProps> = ({ onClick, children, className, disabled, ariaLabel }) => {
    return (
        <button
            className={`${className} ${styles.button}`}
            onClick={onClick}
            disabled={disabled}
            aria-label={ariaLabel}
        >
            {children}
        </button>
    );
};

export default Button;