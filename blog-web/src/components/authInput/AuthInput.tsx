import React from "react";
import styles from "./AuthInput.module.css";

interface AuthInputProps {
    type: "email" | "password";
    value: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    label: string;
    placeholder?: string;
    autoComplete?: string;
    error?: string;
    disabled?: boolean;
}

const AuthInput: React.FC<AuthInputProps> = ({
    type,
    value,
    onChange,
    label,
    placeholder,
    autoComplete,
    error,
    disabled,
}) => {
    return (
        <div className={styles.inputGroup}>
            <label className={styles.label}>
                {label}
                <input
                    type={type}
                    value={value}
                    onChange={onChange}
                    placeholder={placeholder}
                    autoComplete={autoComplete}
                    className={styles.input + (error ? " " + styles.inputError : "")}
                    disabled={disabled}
                />
            </label>
            {error && <span className={styles.error}>{error}</span>}
        </div>
    );
};

export default AuthInput;
