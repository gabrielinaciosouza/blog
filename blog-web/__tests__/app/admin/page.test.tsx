import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import AdminPage from '@/app/admin/page';
import { useRouter } from 'next/navigation';
import { useSearchParams } from 'next/navigation';
import useLoading from '@/hooks/useLoading';

jest.mock('next/navigation', () => ({
    useRouter: jest.fn(),
    useSearchParams: jest.fn(),
}));

jest.mock('@/hooks/useLoading', () => ({
    __esModule: true,
    default: jest.fn(),
}));

jest.mock('@/components/postTile/PostTile', () => () => <div>PostTile</div>);
jest.mock('@/components/button/Button', () => ({ children, onClick }: { children: React.ReactNode, onClick: () => void }) => <button onClick={onClick}>{children}</button>);
jest.mock('@/components/loading/Loading', () => () => <div>Loading...</div>);
jest.mock('@/components/pagination/Pagination', () => () => <div>Pagination</div>);
jest.mock('@/components/tabs/Tabs', () => ({ tabs }: { tabs: { label: string, content: React.ReactNode }[] }) => (
    <div>
        {tabs.map((tab, index) => (
            <div key={index}>
                <button>{tab.label}</button>
                <div>{tab.content}</div>
            </div>
        ))}
    </div>
));

describe('AdminPage', () => {
    const mockRouterPush = jest.fn();
    const mockStartLoading = jest.fn();
    const mockStopLoading = jest.fn();

    beforeEach(() => {
        (useRouter as jest.Mock).mockReturnValue({ push: mockRouterPush });
        (useSearchParams as jest.Mock).mockReturnValue(new URLSearchParams('?page=1'));
        (useLoading as jest.Mock).mockReturnValue({
            isLoading: false,
            startLoading: mockStartLoading,
            stopLoading: mockStopLoading,
        });
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('should render the dashboard with tabs', async () => {
        render(AdminPage());
        expect(screen.getByText('Dashboard')).toBeInTheDocument();
        expect(screen.getByText('New Post')).toBeInTheDocument();
        expect(screen.getByText('Active Posts')).toBeInTheDocument();
        expect(screen.getByText('Deleted Posts')).toBeInTheDocument();
    });

    it('should call handleCreateNewPost when New Post button is clicked', async () => {
        render(<AdminPage />);
        fireEvent.click(screen.getByText('New Post'));
        expect(mockRouterPush).toHaveBeenCalledWith('/create-post');
    });

    it('should display loading component when isLoading is true', async () => {
        (useLoading as jest.Mock).mockReturnValue({
            isLoading: true,
            startLoading: mockStartLoading,
            stopLoading: mockStopLoading,
        });
        render(<AdminPage />);
        expect(screen.getByText('Loading...')).toBeInTheDocument();
    });

    it('should fetch data on mount and when page changes', async () => {
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                ok: true,
                json: async () => [{ postId: '1', title: 'Deleted Post 1', creationDate: '2025-02-25', slug: 'deleted-post-1' }],
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ posts: [{ postId: '2', title: 'Active Post 1', creationDate: '2025-02-25', slug: 'active-post-1' }], totalCount: 1 }),
            });

        render(<AdminPage />);

        await waitFor(() => {
            expect(screen.findAllByText('PostTile')).resolves.toHaveLength(2);
        });

        expect(global.fetch).toHaveBeenCalledTimes(2);
        expect(global.fetch).toHaveBeenCalledWith('/api/posts/deleted');
        expect(global.fetch).toHaveBeenCalledWith('/api/posts?page=1&size=10');
    });

    it('should handle delete post', async () => {
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ posts: [], totalCount: 0 }),
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ message: 'Post deleted successfully' }),
            });

        render(<AdminPage />);

        fireEvent.click(screen.getByRole('button', { name: /delete/i }));

        await waitFor(() => {
            expect(global.fetch).toHaveBeenCalledTimes(2);
        });

        expect(global.fetch).toHaveBeenCalledWith('/api/posts/deleted');
        expect(global.fetch).toHaveBeenCalledWith('/api/posts?page=1&size=10');
    });
});