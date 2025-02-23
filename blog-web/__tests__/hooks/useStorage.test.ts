import { renderHook, act } from '@testing-library/react';
import useStorage from '@/hooks/useStorage';

describe('useStorage', () => {
    beforeEach(() => {
        localStorage.clear();
        jest.clearAllMocks();
    });

    it('should initialize with the value from localStorage if available', () => {
        localStorage.setItem('testKey', JSON.stringify('storedValue'));
        const { result } = renderHook(() => useStorage('testKey', 'initialValue'));

        expect(result.current[0]).toBe('storedValue');
    });

    it('should initialize with the initial value if localStorage is empty', () => {
        const { result } = renderHook(() => useStorage('testKey', 'initialValue'));

        expect(result.current[0]).toBe('initialValue');
    });

    it('should set a new value and store it in localStorage', () => {
        const { result } = renderHook(() => useStorage('testKey', 'initialValue'));

        act(() => {
            result.current[1]('newValue');
        });

        expect(result.current[0]).toBe('newValue');
        expect(localStorage.getItem('testKey')).toBe(JSON.stringify('newValue'));
    });

    it('should handle function updates correctly', () => {
        const { result } = renderHook(() => useStorage('testKey', 0));

        act(() => {
            result.current[1](prev => prev + 1);
        });

        expect(result.current[0]).toBe(1);
        expect(localStorage.getItem('testKey')).toBe(JSON.stringify(1));
    });

    it('should handle errors gracefully', () => {
        jest.spyOn(Storage.prototype, 'setItem').mockImplementation(() => {
            throw new Error('Storage error');
        });

        const { result } = renderHook(() => useStorage('testKey', 'initialValue'));

        act(() => {
            result.current[1]('newValue');
        });

        expect(result.current[0]).toBe('initialValue');
        expect(localStorage.getItem('testKey')).toBeNull();
    });
});