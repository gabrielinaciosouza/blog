import { renderHook, act } from '@testing-library/react';
import useLoading from '@/hooks/useLoading';

describe('useLoading', () => {
    it('should initialize with isLoading set to false', () => {
        const { result } = renderHook(() => useLoading());
        expect(result.current.isLoading).toBe(false);
    });

    it('should set isLoading to true when startLoading is called', () => {
        const { result } = renderHook(() => useLoading());

        act(() => {
            result.current.startLoading();
        });

        expect(result.current.isLoading).toBe(true);
    });

    it('should set isLoading to false when stopLoading is called', () => {
        const { result } = renderHook(() => useLoading());

        act(() => {
            result.current.startLoading();
        });

        act(() => {
            result.current.stopLoading();
        });

        expect(result.current.isLoading).toBe(false);
    });
});