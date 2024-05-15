function filterCustomers(filterType) {
    // Xử lý logic filter tại đây
    // Cập nhật văn bản và số lượng khách hàng tương ứng
    var filterText = document.getElementById('filterText');
    var customerCount = document.getElementById('customerCount');

    switch (filterType) {
        case 'today':
            filterText.innerText = '| Hôm nay';
            // Cập nhật số lượng khách hàng hiển thị
            customerCount.innerText = '[[${totalCustomersToday}]]';
            break;
        case 'thisMonth':
            filterText.innerText = '| Tháng này';
            // Cập nhật số lượng khách hàng hiển thị
            customerCount.innerText = '[[${totalCustomersThisMonth}]]';
            break;
        case 'thisYear':
            filterText.innerText = '| Năm nay';
            // Cập nhật số lượng khách hàng hiển thị
            customerCount.innerText = '[[${totalCustomersThisYear}]]';
            break;
        default:

            break;
    }
}
function filterSales(filterType) {
    var filterText = document.getElementById('filterText1');
    var salesCount = document.getElementById('salesCount');

    switch (filterType) {
        case 'today':
            filterText.innerText = '| Hôm nay';
            salesCount.innerText = '[[${}]]';
            break;
        case 'thisMonth':
            filterText.innerText = '| Tháng này';
            salesCount.innerText =  '[[${}]]';
            break;
        case 'thisYear':
            filterText.innerText = '| Năm nay';
            salesCount.innerText = '[[${}]]';
            break;
        default:
            break;
    }
}
