<template>
	<view>
		<view class="tabs"><u-tabs-swiper ref="uTabs" :list="list" :current="current" @change="tabsChange" :is-scroll="false" :offset="[10, 50]" swiperWidth="750" /></view>
		<swiper :current="swiperCurrent" @transition="transition" @animationfinish="animationfinish" :style="swipperStyle">
			<swiper-item class="swiper-item">
				<scroll-view scroll-y :style="swipperStyle" @scrolltolower="onreachBottom">
					<view class="voucher">
						<view class="left">
							<view class="row"><text class="amount">5.00</text></view>
							<view class="row"><text class="desc">满60元可用</text></view>
						</view>
						<view class="right">
							<view class="row"><text class="desc">仅可用在支付代驾订单中，过期作废，具体解释权归属华夏代驾平台</text></view>
							<view class="row"><text class="desc">2022.03.24 - 2022.04.23</text></view>
						</view>
					</view>
					<view class="voucher">
						<view class="left">
							<view class="row"><text class="amount">15.00</text></view>
							<view class="row"><text class="desc">满60元可用</text></view>
						</view>
						<view class="right">
							<view class="row"><text class="desc">仅可用在支付代驾订单中，过期作废，具体解释权归属华夏代驾平台</text></view>
							<view class="row"><text class="desc">2022.03.24 - 2022.04.23</text></view>
						</view>
					</view>
				</scroll-view>
			</swiper-item>
			<swiper-item class="swiper-item">2</swiper-item>
			<swiper-item class="swiper-item">3</swiper-item>
		</swiper>
	</view>
</template>

<script>
export default {
	data() {
		return {
			list: [
				{
					name: '未使用',
					count: 8
				},
				{
					name: '已使用',
					count: 0
				},
				{
					name: '待领取',
					count: 1
				}
			],
			current: 0, // tabs组件的current值，表示当前活动的tab选项
			swiperCurrent: 0, // swiper组件的current值，表示当前那个swiper-item是活动的
			swipperStyle: ''
		};
	},
	methods: {
		tabsChange(index) {
			this.swiperCurrent = index;
		},
		// swiper-item左右移动，通知tabs的滑块跟随移动
		transition(e) {
			let dx = e.detail.dx;
			this.$refs.uTabs.setDx(dx);
		},
		// 由于swiper的内部机制问题，快速切换swiper不会触发dx的连续变化，需要在结束时重置状态
		// swiper滑动结束，分别设置tabs和swiper的状态
		animationfinish(e) {
			let current = e.detail.current;
			this.$refs.uTabs.setFinishCurrent(current);
			this.swiperCurrent = current;
			this.current = current;
		}
	},
	onLoad: function() {
		let that = this;
		let windowHeight = uni.getSystemInfoSync().windowHeight;
		that.windowHeight = windowHeight;
		that.swipperStyle = `height:${that.windowHeight - 60}px;`;
	}
};
</script>

<style lang="less">
@import url('voucher_list.less');
</style>
