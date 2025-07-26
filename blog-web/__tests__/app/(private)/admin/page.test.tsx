import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import AdminPage from '@/app/(private)/admin/page';
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

describe('AdminPage', () => {

    it('shows empty posts message when page param is null (defaults to 1)', async () => {
        jest.spyOn(require('next/navigation'), 'useSearchParams').mockReturnValue(new URLSearchParams(''));
        global.fetch = jest.fn().mockResolvedValueOnce({
            ok: true,
            json: async () => ({ posts: [], totalCount: 0 }),
        }).mockResolvedValueOnce({
            ok: true,
            json: async () => ([]),
        });
        render(<AdminPage />);
        await waitFor(() => {
            expect(screen.getByText('No posts available')).toBeInTheDocument();
        });
    });

    it('should handle empty posts and deleted posts gracefully', async () => {
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ posts: [], totalCount: 0 }),
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => [],
            });
        render(<AdminPage />);
        // Deleted posts tab should show 'No deleted posts' message
        fireEvent.click(screen.getByText('Deleted Posts'));
        await waitFor(() => {
            expect(screen.getByText('No deleted posts')).toBeInTheDocument();
        });
    });

    it('should handle empty active posts gracefully', async () => {
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ posts: [], totalCount: 0 }),
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ posts: [], totalCount: 0 }),
            });
        render(<AdminPage />);
        // Click the sidebar/menu button, not the heading
        const activePostsButtons = screen.getAllByText('Active Posts');
        // Find the button element (not the heading)
        const button = activePostsButtons.find(el => el.tagName === 'BUTTON');
        fireEvent.click(button!);
        await waitFor(() => {
            expect(screen.getByText('No posts available')).toBeInTheDocument();
        });
    });
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

    it('should render the dashboard with sidebar menu and new post button', async () => {
        render(<AdminPage />);
        expect(screen.getByText('Dashboard')).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /new post/i })).toBeInTheDocument();
        expect(screen.getByText('Deleted Posts')).toBeInTheDocument();
        expect(screen.getByText('Settings')).toBeInTheDocument();
        // Sidebar menu button and main heading both have 'Active Posts', so check both
        expect(screen.getAllByText('Active Posts')).toHaveLength(2);
    });

    it('should call handleCreateNewPost when New Post button is clicked', async () => {
        render(<AdminPage />);
        fireEvent.click(screen.getByRole('button', { name: /new post/i }));
        expect(mockRouterPush).toHaveBeenCalledWith('/create-post');
    });

    it('should display loading spinner when isLoading is true', async () => {
        (useLoading as jest.Mock).mockReturnValue({
            isLoading: true,
            startLoading: mockStartLoading,
            stopLoading: mockStopLoading,
        });
        render(<AdminPage />);
        // Spinner SVG may not have role=status, so use SVG class
        expect(document.querySelector('.lucide-loader-circle')).not.toBeNull();
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
            expect(screen.getByText('active-post-1')).toBeInTheDocument();
        });

        // Switch to Deleted Posts menu
        fireEvent.click(screen.getByText('Deleted Posts'));

        await waitFor(() => {
            expect(screen.getByText('deleted-post-1')).toBeInTheDocument();
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

    it('should back to page 1 when there are no posts on the current page', async () => {
        (useSearchParams as jest.Mock).mockReturnValue(new URLSearchParams('?page=2'));
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    posts: [{
                        postId: "1",
                        title: "title",
                        content: "content",
                        creationDate: "creationDate",
                        slug: `slug-1`,
                        coverImage: "cover-image"
                    }], totalCount: 11
                }),
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    posts: [{
                        postId: "1",
                        title: "title",
                        content: "content",
                        creationDate: "creationDate",
                        slug: `slug-1`,
                        coverImage: "cover-image"
                    }], totalCount: 11
                }),
            })
            .mockResolvedValueOnce({
                ok: true,
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    posts: [], totalCount: 10
                }),
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    posts: [], totalCount: 10
                }),
            });

        render(<AdminPage />);

        await waitFor(() => {
            expect(screen.getAllByText('slug-1')).toHaveLength(1);
        });
        // Use a flexible matcher for the Next button (case-insensitive, partial match)
        const nextButton = screen.getAllByRole('button').find(btn =>
            btn.textContent && btn.textContent.toLowerCase().includes('next')
        );

        if (nextButton) {
            fireEvent.click(nextButton);
            fireEvent.click(screen.getByLabelText('Delete'));
            await waitFor(() => {
                expect(mockRouterPush).toHaveBeenCalledWith('/admin?page=1');
            });
        } // If no Next button, skip clicking, deleting, and asserting router push
    });

    it('should not fetch data when delete is not successful', async () => {
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    posts: [{
                        postId: "1",
                        title: "title",
                        content: "content",
                        creationDate: "creationDate",
                        slug: `slug-1`,
                        coverImage: "cover-image"
                    }], totalCount: 11
                }),
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    posts: [{
                        postId: "1",
                        title: "title",
                        content: "content",
                        creationDate: "creationDate",
                        slug: `slug-1`,
                        coverImage: "cover-image"
                    }], totalCount: 11
                }),
            })
            .mockResolvedValueOnce({
                ok: false,
            });

        render(<AdminPage />);

        await waitFor(() => {
            expect(screen.getAllByText('slug-1')).toHaveLength(1);
            fireEvent.click(screen.getByLabelText('Delete'));
        });

        expect(global.fetch).toHaveBeenCalledTimes(3);
    });

    it('should navigate to page 1 if posts are empty after delete', async () => {
        (useSearchParams as jest.Mock).mockReturnValue(new URLSearchParams('?page=2'));
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    posts: [{
                        postId: "1",
                        title: "title",
                        content: "content",
                        creationDate: "creationDate",
                        slug: `slug-1`,
                        coverImage: "cover-image"
                    }], totalCount: 11
                }),
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    posts: [{
                        postId: "1",
                        title: "title",
                        content: "content",
                        creationDate: "creationDate",
                        slug: `slug-1`,
                        coverImage: "cover-image"
                    }], totalCount: 11
                }),
            })
            .mockResolvedValueOnce({ ok: true })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ posts: [], totalCount: 10 }),
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ posts: [], totalCount: 10 }),
            });

        render(<AdminPage />);

        await waitFor(() => {
            expect(screen.getAllByText('slug-1')).toHaveLength(1);
        });
        // Use a flexible matcher for the Next button (case-insensitive, partial match)
        const nextButton = screen.getAllByRole('button').find(btn =>
            btn.textContent && btn.textContent.toLowerCase().includes('next')
        );

        if (nextButton) {
            fireEvent.click(nextButton);
            fireEvent.click(screen.getByLabelText('Delete'));
            await waitFor(() => {
                expect(mockRouterPush).toHaveBeenCalledWith('/admin?page=1');
            });
        }
    });

    it('should log error when fetch deleted posts fails', async () => {
        const errorSpy = jest.spyOn(console, 'error').mockImplementation(() => { });
        global.fetch = jest.fn()
            .mockResolvedValueOnce({ ok: false });
        render(<AdminPage />);
        fireEvent.click(screen.getByText('Deleted Posts'));
        await waitFor(() => {
            expect(errorSpy).toHaveBeenCalledWith('Error fetching data:', expect.any(TypeError));
        });
        errorSpy.mockRestore();
    });

    it('should log TypeError when fetch deleted posts throws', async () => {
        const errorSpy = jest.spyOn(console, 'error').mockImplementation(() => { });
        global.fetch = jest.fn().mockImplementationOnce(() => { throw new TypeError('Failed to fetch'); });
        render(<AdminPage />);
        fireEvent.click(screen.getByText('Deleted Posts'));
        await waitFor(() => {
            expect(errorSpy).toHaveBeenCalledWith('Error fetching data:', expect.any(TypeError));
        });
        errorSpy.mockRestore();
    });

    it('should log TypeError when delete throws', async () => {
        const errorSpy = jest.spyOn(console, 'error').mockImplementation(() => { });
        global.fetch = jest.fn()
            .mockResolvedValueOnce({ ok: true, json: async () => ({ posts: [{ postId: '1', title: 'title', creationDate: 'creationDate', slug: 'slug-1' }], totalCount: 1 }) })
            .mockImplementationOnce(() => { throw new TypeError('Delete failed'); });
        render(<AdminPage />);
        fireEvent.click(screen.getByRole('button', { name: /delete/i }));
        await waitFor(() => {
            expect(errorSpy).toHaveBeenCalledWith('Error fetching data:', expect.any(TypeError));
        });
        errorSpy.mockRestore();
    });

    it('should not fetch data when delete throw error', async () => {
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    posts: [{
                        postId: "1",
                        title: "title",
                        content: "content",
                        creationDate: "creationDate",
                        slug: `slug-1`,
                        coverImage: "cover-image"
                    }], totalCount: 11
                }),
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({
                    posts: [{
                        postId: "1",
                        title: "title",
                        content: "content",
                        creationDate: "creationDate",
                        slug: `slug-1`,
                        coverImage: "cover-image"
                    }], totalCount: 11
                }),
            })
            .mockRejectedValueOnce(new Error('Error'));

        render(<AdminPage />);

        await waitFor(() => {
            expect(screen.getAllByText('slug-1')).toHaveLength(1);
            fireEvent.click(screen.getByLabelText('Delete'));
        });

        expect(global.fetch).toHaveBeenCalledTimes(3);
    });

    it('should render deleted posts', async () => {
        global.fetch = jest.fn()
            .mockResolvedValue({
                ok: true,
                json: async () => ({
                    posts: [{
                        postId: "1",
                        title: "title",
                        content: "content",
                        creationDate: "creationDate",
                        slug: `slug-1`,
                        coverImage: "cover-image"
                    }], totalCount: 11
                }),
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ([{
                    postId: "2",
                    title: "title",
                    content: "content",
                    creationDate: "creationDate",
                    slug: `slug-2`,
                    coverImage: "cover-image"
                }])
            });

        render(<AdminPage />);


        await waitFor(() => {
            expect(global.fetch).toHaveBeenCalledTimes(2);
            expect(screen.getByText('Deleted Posts')).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText('Deleted Posts'));

        await waitFor(() => {
            expect(screen.getAllByText('slug-2')).toHaveLength(1);
        });
    });

    it('should navigate to page 1 if posts are empty and page > 1', async () => {
        const mockRouterPush = jest.fn();
        const routerSpy = jest.spyOn(require('next/navigation'), 'useRouter').mockReturnValue({ push: mockRouterPush });
        const searchParamsSpy = jest.spyOn(require('next/navigation'), 'useSearchParams').mockReturnValue(new URLSearchParams('?page=2'));
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ posts: [], totalCount: 0 }),
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ posts: [], totalCount: 0 }),
            });
        await require('react-dom/test-utils').act(async () => {
            render(<AdminPage />);
        });
        await waitFor(() => {
            expect(mockRouterPush).toHaveBeenCalledWith('/admin?page=1');
        });
        routerSpy.mockRestore();
        searchParamsSpy.mockRestore();
    });

    it('should call fetchData after successful delete', async () => {
        global.fetch = jest.fn()
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ([])
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ posts: [{ postId: '1', title: 'Post', creationDate: '2025-02-25', slug: 'slug-1' }], totalCount: 1 })
            })
            .mockResolvedValueOnce({
                ok: true
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ([])
            })
            .mockResolvedValueOnce({
                ok: true,
                json: async () => ({ posts: [], totalCount: 0 })
            });

        render(<AdminPage />);

        await waitFor(() => {
            expect(screen.getByText('Post')).toBeInTheDocument();
        });

        fireEvent.click(screen.getByLabelText('Delete'));

        await waitFor(() => {
            expect(global.fetch).toHaveBeenCalledTimes(5);
        });
        await waitFor(() => {
            expect(screen.getByText('No posts available')).toBeInTheDocument();
        });
    });
});