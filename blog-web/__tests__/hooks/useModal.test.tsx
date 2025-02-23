import { renderHook, act } from '@testing-library/react';
import { useModal } from '@/hooks/useModal';

describe('useModal', () => {
    it('should initialize with modal closed', () => {
        const { result } = renderHook(() => useModal());
        expect(result.current.modalState.isOpen).toBe(false);
        expect(result.current.modalState.content).toBeNull();
    });

    it('should open the modal with provided content and onClose callback', () => {
        const { result } = renderHook(() => useModal());
        const content = <div>Modal Content</div>;
        const onClose = jest.fn();

        act(() => {
            result.current.openModal(content, onClose);
        });

        expect(result.current.modalState.isOpen).toBe(true);
        expect(result.current.modalState.content).toBe(content);
        expect(result.current.modalState.onClose).toBe(onClose);
    });

    it('should close the modal and call onClose callback', () => {
        const { result } = renderHook(() => useModal());
        const content = <div>Modal Content</div>;
        const onClose = jest.fn();

        act(() => {
            result.current.openModal(content, onClose);
        });

        act(() => {
            result.current.closeModal();
        });

        expect(result.current.modalState.isOpen).toBe(false);
        expect(result.current.modalState.content).toBeNull();
        expect(onClose).toHaveBeenCalled();
    });

    it('should not throw an error if onClose is not provided', () => {
        const { result } = renderHook(() => useModal());
        const content = <div>Modal Content</div>;

        act(() => {
            result.current.openModal(content, () => {});
        });

        act(() => {
            result.current.closeModal();
        });

        expect(result.current.modalState.isOpen).toBe(false);
        expect(result.current.modalState.content).toBeNull();
    });
});