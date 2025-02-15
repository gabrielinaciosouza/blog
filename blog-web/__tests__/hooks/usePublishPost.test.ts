import { renderHook, act } from '@testing-library/react';
import { usePublishPost } from '@/hooks/usePublishPost';

const localStorageMock = (() => {
    let store: Record<string, string> = {};
    return {
        getItem: (key: string): string | null => store[key] || null,
        setItem: (key: string, value: string) => {
            store[key] = value;
        },
        removeItem: (key: string) => {
            delete store[key];
        },
        clear: () => {
            store = {};
        },
    };
})();

Object.defineProperty(window, 'localStorage', { value: localStorageMock });

jest.mock('@/services/postService', () => ({
    createPost: jest.fn(),
}));

const routerPush = jest.fn();
jest.mock("next/navigation", () => ({
    useRouter() {
        return {
            push: routerPush,
        };
    }
}));

describe('usePublishPost Hook', () => {
    let mockCreatePost: jest.Mock;

    beforeEach(() => {
        localStorageMock.clear();
        jest.clearAllMocks();
        mockCreatePost = require('@/services/postService').createPost;
    });

    it('should load saved draft from localStorage', () => {
        localStorageMock.setItem('draft-title', 'Saved Title');
        localStorageMock.setItem('draft-content', 'Saved Content');

        const { result } = renderHook(() => usePublishPost());

        expect(result.current.title).toBe('Saved Title');
        expect(result.current.content).toBe('Saved Content');
    });

    it('should update localStorage when title and content change', () => {
        const { result } = renderHook(() => usePublishPost());

        act(() => {
            result.current.setTitle('New Title');
            result.current.setContent('New Content');
        });

        expect(localStorageMock.getItem('draft-title')).toBe('New Title');
        expect(localStorageMock.getItem('draft-content')).toBe('New Content');
    });

    it('should remove draft from localStorage upon successful publish', async () => {
        mockCreatePost.mockResolvedValue({ postId: '123' });

        const { result } = renderHook(() => usePublishPost());

        act(() => {
            result.current.setTitle('Final Post');
            result.current.setContent('Final Content');
        });

        await act(async () => {
            await result.current.handlePublish();
        });

        act(() => {
            result.current.handleCloseModal();
        });

        expect(localStorageMock.getItem('draft-title')).toBeNull();
        expect(localStorageMock.getItem('draft-content')).toBeNull();
        expect(result.current.responseMessage).toBe('Post saved successfully!');
    });

    it('should keep draft in localStorage if publish fails', async () => {
        mockCreatePost.mockRejectedValue(new Error('API Error'));

        const { result } = renderHook(() => usePublishPost());

        act(() => {
            result.current.setTitle('Failed Post');
            result.current.setContent('Failed Content');
        });

        await act(async () => {
            await result.current.handlePublish();
        });

        expect(localStorageMock.getItem('draft-title')).toBe('Failed Post');
        expect(localStorageMock.getItem('draft-content')).toBe('Failed Content');
        expect(result.current.responseMessage).toBe('API Error');
    });

    it('should redirect upon closing the modal after successful publish', async () => {
        mockCreatePost.mockResolvedValue({ slug: 'any-title' });

        const { result } = renderHook(() => usePublishPost());

        act(() => {
            result.current.setTitle('Test Title');
            result.current.setContent('Test Content');
        });

        await act(async () => {
            await result.current.handlePublish();
        });

        expect(result.current.responseMessage).toBe('Post saved successfully!');

        act(() => {
            result.current.handleCloseModal();
        });

        expect(routerPush).toHaveBeenCalledTimes(1);
        expect(routerPush).toHaveBeenCalledWith('/posts/any-title');
    });

    it('should show error message if title or content is empty', async () => {
        const { result } = renderHook(() => usePublishPost());

        await act(async () => {
            await result.current.handlePublish();
        });

        expect(result.current.responseMessage).toBe('Title and/or content cannot be empty');
        expect(result.current.showModal).toBe(true);
    });

});