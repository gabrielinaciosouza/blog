import React from "react";
import styles from "./button.module.css";

interface ButtonProps {
    onClick: () => void;
    children: React.ReactNode;
    className?: string;
    disabled?: boolean;
}

const Button: React.FC<ButtonProps> = ({ onClick, children, className, disabled }) => {
    return (
        <button
            className={`${className} ${styles.button}`}
            onClick={onClick}
            disabled={disabled}
        >
            {children}
        </button>
    );
};

export default Button;