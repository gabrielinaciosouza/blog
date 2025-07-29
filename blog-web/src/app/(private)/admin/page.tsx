
import React, { Suspense } from 'react';
import AdminDashboardWrapper from './AdminDashboardWrapper';

const AdminPage = () => {
    return (
        <Suspense fallback={<div>Loading...</div>}>
            <AdminDashboardWrapper />
        </Suspense>
    );
};

export default AdminPage;