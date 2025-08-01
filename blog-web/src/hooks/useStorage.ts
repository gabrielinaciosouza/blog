import { useState } from 'react';

function useStorage<T>(key: string, initialValue: T) {
    const [storedValue, setStoredValue] = useState<T>(() => {
        if (typeof window === "undefined" || !window.localStorage) {
            return initialValue;
        }
        try {
            const item = window.localStorage.getItem(key);
            return item ? JSON.parse(item) : initialValue;
        } catch (error) {
            console.error(error);
            return initialValue;
        }
    });

    const setValue = (value: T | ((val: T) => T)) => {
        try {
            const valueToStore = value instanceof Function ? value(storedValue) : value;
            localStorage.setItem(key, JSON.stringify(valueToStore));
            setStoredValue(valueToStore);
        } catch (error) {
            console.error(error);
        }
    };

    return [storedValue, setValue] as const;
}

export default useStorage;